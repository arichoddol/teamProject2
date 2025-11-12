// package org.spring.backendspring.board.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// // @Controller를 사용하여 전통적인 MVC 방식으로 구현합니다.
// // 작성 후 뷰 이름(redirect: 경로)을 반환합니다.
// @Controller
// public class tmp {

//     // 클라이언트의 <form action="/board/write" method="post" ...> 요청을 처리합니다.
//     @PostMapping("/board/write")
//     public String writeBoardPost(
//             // 폼 필드들을 @RequestParam으로 받습니다.
//             @RequestParam("memberId") String memberId,
//             @RequestParam("title") String title,
//             @RequestParam("boardPw") String boardPw,
//             @RequestParam("content") String content,
//             @RequestParam("nickName") String nickName,
            
//             // 파일 필드는 MultipartFile 타입으로 받습니다.
//             @RequestParam("boardFile") MultipartFile boardFile, 
            
//             // 리다이렉트 시 메시지를 전달하기 위한 객체입니다.
//             RedirectAttributes redirectAttributes) {

//         // --- 1. 유효성 검사 및 비즈니스 로직 처리 시작 ---

//         // 실제 구현에서는 이 데이터를 BoardService로 전달하여 DB에 저장합니다.
//         // boardService.savePost(memberId, title, boardPw, content, nickName);
        
//         // 콘솔에 받은 데이터 출력 (실제 로직 대신 사용)
//         System.out.println("==========================================");
//         System.out.println("새 게시글 작성 요청 데이터 수신:");
//         System.out.println(" - MEMBER_ID: " + memberId);
//         System.out.println(" - 글제목: " + title);
//         System.out.println(" - Password: " + boardPw);
//         System.out.println(" - NickName: " + nickName);
        
//         // --- 2. 파일 업로드 처리 로직 ---
//         if (boardFile != null && !boardFile.isEmpty()) {
//             String originalFileName = boardFile.getOriginalFilename();
//             long fileSize = boardFile.getSize();
//             String contentType = boardFile.getContentType();
            
//             System.out.println(" - 첨부 파일 정보:");
//             System.out.println("    - 파일명: " + originalFileName);
//             System.out.println("    - 파일 크기: " + fileSize + " bytes");
//             System.out.println("    - Content-Type: " + contentType);
            
//             // TODO: 
//             // 1. 서버의 특정 경로에 boardFile.transferTo(new File("경로/파일명")); 를 사용하여 파일을 저장합니다.
//             // 2. 저장된 파일의 경로 및 정보를 DB에 저장하는 로직을 추가합니다.
            
//         } else {
//             System.out.println(" - 첨부 파일: 없음");
//         }
        
//         // --- 3. 처리 완료 후 결과 전송 ---
        
//         // 게시글 작성이 성공적으로 완료되었다는 메시지를 다음 요청(리다이렉트)에 전달합니다.
//         redirectAttributes.addFlashAttribute("message", "게시글 작성이 완료되었습니다!");

//         // 게시글 목록 페이지(예: /board/index)로 리다이렉트합니다.
//         return "redirect:/board/index";
//     }
// } {
    
// }
