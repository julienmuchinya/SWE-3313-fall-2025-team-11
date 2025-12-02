package artstore.controller.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
public class AdminPageController{

    @GetMapping("/admin-page")
    public String adminPage() {
        return "admin-page";
    }
    @PostMapping("/admin-page")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (!file.isEmpty()) {
            // Convert file to Base64 to embed directly in HTML
            String base64Image = null;
            try {
                base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            //Variable right here can be saved to the db and loaded by thymeleaf
            String dataUrl = "data:" + file.getContentType() + ";base64," + base64Image;
            model.addAttribute("imageUrl", dataUrl);
        }

        return "admin-page";
    }

}

