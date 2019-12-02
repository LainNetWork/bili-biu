package fun.lain.bilibiu.web.controller;

import fun.lain.bilibiu.common.Echo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MonitorController {

    @RequestMapping("/back")
    public String  toIndex(){
        return "index";
    }
}
