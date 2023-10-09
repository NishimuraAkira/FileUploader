package jp.co.patsystem.spring.uploader.uploader.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.patsystem.spring.uploader.uploader.forms.UploadForm;

/***
 * ファイルアップロードのコントローラークラス
 */
@Controller
public class FileUploadController {

    /**
     * 初期処理
     * 
     * @return
     */
    @GetMapping
    String index(Model model) {
        // アップロードファイルをモデルにセット
        model.addAttribute("uploadForm", new UploadForm());

        // アップロード画面に遷移
        return "/index";
    }

    /**
     * ファイルアップロード処理
     * 
     * @param uploadForm
     * @return
     */
    @PostMapping("/upload")
    String upload(UploadForm uploadForm) {
        // フォームで渡されてきたアップロードファイルを取得
        List<MultipartFile> multipartFile = uploadForm.getMultipartFile();

        // ===== 変更1：コントローラークラスに複数ファイルを1つずつにする処理に変更 ===== //
        multipartFile.forEach(e -> {
            // アップロード実行処理メソッド呼び出し
            uploadAction(e);
        });

        // リダイレクト
        return "redirect:/";
    }

    /**
     * アップロード実行処理
     * 
     * @param multipartFile
     */
    private void uploadAction(MultipartFile multipartFile) {
        // ファイル名取得
        String fileName = multipartFile.getOriginalFilename();

        // 格納先のフルパス ※事前に格納先フォルダ「UploadTest」をCドライブ直下に作成しておく
        Path filePath = Paths.get("D:/Temp/uploadFiles/" + fileName);

        try {
            // アップロードファイルをバイト値に変換
            byte[] bytes = multipartFile.getBytes();

            // バイト値を書き込む為のファイルを作成して指定したパスに格納
            OutputStream stream = Files.newOutputStream(filePath);

            // ファイルに書き込み
            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
