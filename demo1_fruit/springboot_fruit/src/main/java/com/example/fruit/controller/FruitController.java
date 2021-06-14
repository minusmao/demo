package com.example.fruit.controller;


import com.example.fruit.entity.Fruit;
import com.example.fruit.service.FruitService;
import com.example.fruit.service.impl.FruitServiceImpl;
import com.example.fruit.vo.FruitBarVO;
import com.example.fruit.vo.FruitPieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author TechRice
 * @since 2021-06-12
 */
@RestController
@RequestMapping("//fruit")
public class FruitController {
    /* 对象属性 */
    @Autowired
    private final FruitService fruitService = new FruitServiceImpl();    // 注入 Service 层

    /* 对象方法 */
    @GetMapping("/list")
    public List<Fruit> list() {
        // 调用 Service 层
        List<Fruit> fruitList = this.fruitService.list();

        return fruitList;
    }
    @DeleteMapping("/delete/{id}")
    public boolean delete(@PathVariable("id") Integer id) {
        // 调用 Service 层
        return this.fruitService.removeById(id);
    }
    @GetMapping("/find/{id}")
    public Fruit find(@PathVariable("id") Integer id) {
        // 调用 Service 层
        return this.fruitService.getById(id);
    }
    @PutMapping("/update")
    public boolean update(@RequestBody Fruit fruit) {
        // 调用 Service 层
        return this.fruitService.updateById(fruit);    // 会根据传入 fruit 的 id 属性更新
    }
    @PutMapping("/add")
    public boolean add(@RequestBody Fruit fruit) {
        // 调用 Service 层
        return this.fruitService.save(fruit);
    }
    @GetMapping("/listPieVO")
    public List<FruitPieVO> listPieVO() {
        // 调用 Service 层
        return this.fruitService.listFruitPieVO();
    }
    @GetMapping("/getBarVO")
    public FruitBarVO getBarVO() {
        // 调用 Service 层
        return this.fruitService.getFruitBarVO();
    }
}

