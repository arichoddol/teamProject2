package org.spring.backendspring.board.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.spring.backendspring.board.entity.BoardEntity;
import org.spring.backendspring.board.entity.BoardImgEntity;
import org.spring.backendspring.board.entity.BoardReplyEntity;
import org.spring.backendspring.member.entity.MemberEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {


    public BoardDto(String title, String content, Long memberId, String nickName) {
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.memberNickName = nickName;

        // tmp function
    }


    

    private Long id;
 
    private String title;
  
    private String content;


    private Long memberId; 

    // this declare for Check Exist File
    private int attachFile;

    // createTime
    private LocalDateTime createTime;
    // updateTime 
    private LocalDateTime updateTime;

    private int replyCount;
    private int hit;
    private String memberNickName;

    private MultipartFile boardFile;
    private List<MultipartFile> boardFileList;

    private String newFileName;
    private String oldFileName;

    // N:1
    private MemberEntity memberentity;

    // 1:N
    private List<BoardReplyEntity> boardReplyEntities;
    private List<BoardImgEntity> boardImgEntities;


    // toDto
    public static BoardDto toBoardDto( BoardEntity boardEntity) {
        String newFileName = null;
        String oldFileName = null;

        if(boardEntity.getAttachFile() != 0 && 
            boardEntity.getBoardImgEntities() != null &&
            !boardEntity.getBoardImgEntities().isEmpty()){
                newFileName = boardEntity.getBoardImgEntities().get(0).getNewName();
                oldFileName = boardEntity.getBoardImgEntities().get(0).getOldName();
            }

            return BoardDto.builder()
                    .id(boardEntity.getId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .hit(boardEntity.getHit())
                    .memberNickName(boardEntity.getMemberEntity().getNickName())
                    .createTime(boardEntity.getCreateTime())
                    .updateTime(boardEntity.getUpdateTime())
                    .attachFile(boardEntity.getAttachFile()) // 0 또는 1
            
                    .newFileName(newFileName) 
                    .oldFileName(oldFileName)
            
                    .build();
    }
         
    
}
