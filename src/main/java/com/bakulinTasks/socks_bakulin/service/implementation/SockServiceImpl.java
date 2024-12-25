package com.bakulinTasks.socks_bakulin.service.implementation;

import com.bakulinTasks.socks_bakulin.dto.SocksDTO;
import com.bakulinTasks.socks_bakulin.dto.SocksInfoDTO;
import com.bakulinTasks.socks_bakulin.dto.SocksSpecificationDTO;
import com.bakulinTasks.socks_bakulin.exception.ModelNotFoundException;
import com.bakulinTasks.socks_bakulin.exception.WrongDataException;
import com.bakulinTasks.socks_bakulin.model.SocksModel;
import com.bakulinTasks.socks_bakulin.repository.SocksModelSpecification;
import com.bakulinTasks.socks_bakulin.repository.SocksRepository;
import com.bakulinTasks.socks_bakulin.service.SocksService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SockServiceImpl implements SocksService {
    private final SocksRepository socksRepository;
    Logger log = LoggerFactory.getLogger(SockServiceImpl.class);
    @Override
    public SocksDTO addInStorage(SocksInfoDTO info) {
        SocksModel model = new SocksModel();
        log.info("Проверка данных для фиксации прихода носков");
        if(info.getColour().isEmpty()||info.getQuantity()==null||info.getCottonContent()==null)
            throw new WrongDataException("Поля не могут быть пустыми!");
        if(info.getCottonContent()<0||info.getCottonContent()>100)
            throw new WrongDataException("Неверные данные о процентном соотношении хлопка!");
        if(info.getQuantity()<=0)
            throw new WrongDataException("Неверные данные о количестве!");
        log.info("Данные прихода носков проверены");
        if(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(info.getColour(), info.getCottonContent()).isEmpty()) {
            model.setColour(info.getColour());
            model.setCottonContent(info.getCottonContent());
            model.setQuantity(info.getQuantity());
            SocksModel result = socksRepository.save(model);
            log.info("Данные успешно сохранены. На склад пришло "+info.getQuantity()+" носков");
            return toDTO(result);
        }
        else {
            model = socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(info.getColour(), info.getCottonContent()).get();
            model.setQuantity(model.getQuantity()+ info.getQuantity());
            SocksModel result = socksRepository.save(model);
            log.info("Данные успешно сохранены.На склад пришло "+info.getQuantity()+" носков");
            return toDTO(socksRepository.save(result));
        }
    }
    /**Если после получения носков в оставшейся партии количество 0, то запись удаляется**/
    @Override
    public SocksDTO getFromStorage(SocksInfoDTO info) {
        log.info("Проверка данных выдачи носков");
        if(socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(info.getColour(), info.getCottonContent()).isEmpty())
            throw new WrongDataException("Носков такого типа нет на складе!");
        SocksModel model = socksRepository.findSocksModelByColourIgnoreCaseAndCottonContent(info.getColour(), info.getCottonContent()).get();
          if(model.getQuantity()<info.getQuantity())
              throw new WrongDataException("Такого количества носков на складе нет!");
          log.info("Данные выдачи носков проверены");
          model.setQuantity(model.getQuantity()- info.getQuantity());
        if(model.getQuantity()>0) {
          SocksModel result = socksRepository.save(model);
        log.info("Новые данные сохранены. Взято со склада "+info.getQuantity()+" пар носков, остаток - "+result.getQuantity()+" пар носков");
        return toDTO(result);}
        else {socksRepository.delete(model);
            log.info("Носки взяты со склада. Носков этой партии больше не осталось");
            return null;}
    }
    /**Файл должен иметь заголовки ячеек "Цвет носков", "Содержание хлопка", "Количество"**/
    @Override
    public List<SocksDTO> readUsersFromExcel(MultipartFile inputStream) {
        List<SocksDTO> listOfInfo = new ArrayList<>();
        log.info("Начало работы с файлом Excel");
        try (Workbook workbook = new XSSFWorkbook(inputStream.getInputStream())) {
            if (isRightNamesOfColumns(workbook)) {
                Sheet sheet = workbook.getSheetAt(0);
                Row initialRow = sheet.getRow(0);
                int cellColour = 0;
                int cellCotton = 0;
                int cellQuantity = 0;
                for (Cell cell : initialRow) {
                    if (getCellValue(cell).equalsIgnoreCase("Цвет носков"))
                        cellColour = cell.getColumnIndex();
                    if (getCellValue(cell).equalsIgnoreCase("Содержание хлопка"))
                        cellCotton = cell.getColumnIndex();
                    if (getCellValue(cell).equalsIgnoreCase("Количество"))
                        cellQuantity = cell.getColumnIndex();
                }

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    SocksInfoDTO info = new SocksInfoDTO();
                    log.info("Разбор данных из ячеек "+row.getRowNum()+" строки");
                    for (Cell cell : row) {
                       if (cell.getColumnIndex()==cellColour) {
                           info.setColour(getCellValue(cell));
                       }
                        if (cell.getColumnIndex()==cellCotton) {
                            info.setCottonContent(Double.valueOf(getCellValue(cell)));
                        }
                        if (cell.getColumnIndex()==cellQuantity) {
                            info.setQuantity(Integer.valueOf(getCellValue(cell)));
                        }
                        log.info("Разбор успешно завершён!");
                    }
                    log.info("Сохранение данных в базу");
                    listOfInfo.add(addInStorage(info));
                    log.info("Данные успешно сохранены");
                }

            } else throw new WrongDataException("Неверная структура excel файла! Названия столбцов должны быть: \"Цвет носков\", \"Содержание хлопка\", \"Количество\"");

        } catch (EncryptedDocumentException |IOException ex) {
            log.error("Проблемы с файлом");
        }
        log.info("Все данные по приходу носок на склад из Excel успешно сохранены");
        return listOfInfo;
    }
  /**Если при изменении записей выясниться, что появились две одинаковые записи (по цветуи соотношению хлопка)
   * то они будут объединены в одну, с id изменяемой записи**/
    @Override
    public SocksDTO updateInStorage(Integer id, SocksInfoDTO info) {
        log.info("Проверка данных для изменения записи в базе");
        SocksModel model = socksRepository.findById(id).orElseThrow(()->new ModelNotFoundException("Такая запись не найдена"));
        if(info.getColour().isEmpty()||info.getQuantity()==null||info.getCottonContent()==null)
            throw new WrongDataException("Поля не могут быть пустыми!");
        if(info.getCottonContent()<0||info.getCottonContent()>100)
            throw new WrongDataException("Неверные данные о процентном соотношении хлопка!");
        if(info.getQuantity()<=0)
            throw new WrongDataException("Неверные данные о количестве!");
        log.info("Данные успешно проверены");
        model.setQuantity(info.getQuantity());
        model.setCottonContent(info.getCottonContent());
        model.setColour(info.getColour());
        log.info("Обновление данных в базе");
        SocksModel result = socksRepository.save(model);
        List<SocksModel> listOfEqualModels = socksRepository.findSocksModelsByColourIgnoreCaseAndCottonContent(info.getColour(),info.getCottonContent())
                .stream().filter(a->!a.getId().equals(id)).toList();
        if(!listOfEqualModels.isEmpty())
        {result.setQuantity(listOfEqualModels.get(0).getQuantity()+result.getQuantity());
          socksRepository.delete(listOfEqualModels.get(0));
          SocksModel res = socksRepository.save(result);
            log.info("Данные успешно обновлены!");
          return toDTO(res);}
        else {log.info("Данные успешно обновлены");
            return toDTO(result);}
    }
  /** Метод возвращает мапу, где ключом является количество носков в найденных партиях,
   * а значением - список партий, отсортированный по содержанию хлопка**/
    @Override
    public Map<Integer,List<SocksDTO>> getSocksQuantityWithFilter(String colour, Double cottonContent, Double lessThanCottonContent, Double moreThanCottonContent) {
        log.info("Установка значений для фильтрации");
        SocksSpecificationDTO model = new SocksSpecificationDTO();
        model.setColour(colour);
        model.setMoreThanCottonContent(moreThanCottonContent);
        model.setLessThanCottonContent(lessThanCottonContent);
        model.setCottonContent(cottonContent);
        Specification<SocksModel> specification = SocksModelSpecification.filterSocks(model);
        log.info("Значения установлены");
        log.info("Получение данных");
        List<SocksDTO> res = socksRepository.findAll(specification).stream().map(this::toDTO).sorted(Comparator.comparing(SocksDTO::getCottonContent)).toList();
        List<Integer> listOfQuantity = res.stream().map(SocksDTO::getQuantity).toList();
        Integer result = 0;
        for (Integer q: listOfQuantity) {
            result = result+q;
        }
        log.info("Данные успешно получены");
        Map<Integer,List<SocksDTO>> commonResult = new HashMap<>();
        commonResult.put(result,res);
        return commonResult;
    }

    public SocksDTO toDTO(SocksModel model) {
        SocksDTO dto = new SocksDTO();
        dto.setId(model.getId());
        dto.setColour(model.getColour());
        dto.setCottonContent(model.getCottonContent());
        dto.setQuantity(model.getQuantity());
        return dto;
    }
    private boolean isRightNamesOfColumns(Workbook workbook) {
        List<String> columns = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        Row initialRow = sheet.getRow(0);
        for (Cell cell : initialRow) {
            columns.add((cell.getStringCellValue()).toLowerCase());
        }
        return columns.contains("цвет носков") && columns.contains("содержание хлопка") && columns.contains("количество");
    }

    private String getCellValue(Cell cell) {
        String value = null;
        if (cell != null) {
            CellType cellType = cell.getCellType();
            if (cellType == CellType.NUMERIC) {
                double numericCellValue = cell.getNumericCellValue();
                value = numericCellValue % 1 == 0 ? String.valueOf((long) numericCellValue) : String.valueOf(numericCellValue);
            }
            if (cellType == CellType.STRING) {
                value = cell.getStringCellValue();
            }
        }
        return value;
    }
}
