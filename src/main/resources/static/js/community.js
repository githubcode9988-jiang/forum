/**
 *  提交回复
 */
function post(){
    var parentId = $("#question_id").val();
    var comment = $("#comment_content").val();
    comment2target(parentId,1,comment);
}

/**
 *  提交评论
 * @param targetId
 * @param type
 * @param content
 */
function comment(e){
       var commentId = e.getAttribute('data-id');
       var content = $("#input-"+commentId).val();
       comment2target(commentId,2,content);
}

/**
 *  评论，问题，公共提交
 * @param targetId
 * @param type
 * @param content
 */
function comment2target(targetId,type,content){
    if(!comment){
        alert("不能回复空内容！");
        return;
    }

    $.ajax({
        type: "POST",
        contentType:"application/json",
        dataType:"json",
        url: "/comment",
        data: JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function(response){
            if(response.code == 200){
                window.location.reload();
            }else{
                if(response.code == 2003){
                    var isAccepted = confirm(response.message);
                    if(isAccepted){
                        window.open("https://gitee.com/oauth/authorize?client_id=e525273cb1abea8c1840042fc9546a547ebebf732fd7fcd89034673452ec7210&redirect_uri=http://localhost:8080/callback&response_type=code&state=1");
                        window.localStorage.setItem("closable",true);
                    }
                }else{
                    alert(response.message);
                }
            }
        }
    });
}


/**
 *  展开二级评论
 */
function collapseComments(e){
     var id = e.getAttribute("data-id");
     var comments = $("#comment-"+id);
     //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if(collapse){
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    }else{
        var subCommentContainer = $("#comment-"+id);
        var flag = false;
        getSecondComment(id,subCommentContainer,flag);
        if(!flag){
                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse","in");
                e.classList.add("active");
        }
    }
}

/**
 *  设置标签
 * @param value
 */
function selectTag(e){
     var previous = $("#tag").val();
     var value = e.getAttribute("data-tag");
     if(previous.indexOf(value) == -1){
         if(previous){
             $("#tag").val(previous + ',' + value);
         }else{
             $("#tag").val(value);
         }
     }

}

/**
 *  展示Tag标签
 */
function showSelectTag(){
    $("#select-tag").show();
}

/**
 * 获取二级评论
 * @param id
 * @param subCommentContainer
 * @param flag
 */
function getSecondComment(id,subCommentContainer,flag){
    $.getJSON("/comment/"+id,function(data){
        if(data.data.length >= 1){
            flag = true;
        }
        $.each(data.data,function (index,comment){

            var mediaLeftElement = $("<div/>",{
                "class":"media-left"
            }).append($("<img/>",{
                "class":"media-object img-circle",
                "src": comment.user.avatarUrl
            }));

            var mediaBodyElement = $("<div/>",{
                "class":"media-body comments",
            }).append($("<h5/>",{
                "html":comment.user.name
            })).append($("<div/>",{
                "html":comment.content
            })).append($("<div/>",{
                "class":"menu",
            })).append($("<div/>",{
                "class":"pull-right",
                "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
            }))

            var mediaElement = $("<div/>",{
                "class":"media"
            }).append(mediaLeftElement).append(mediaBodyElement)

            var com = $("<div/>",{
                "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
            }).append(mediaElement);

            subCommentContainer.prepend(com);
        });
    });
}

/**
 *  邀请询问作者
 */
function inquiry(){
    var inputValue = $("#question_inquiry").val();
    var question_id = $("#question_id").val();
    if(inputValue == null){
        alert("请输入你的消息！");
    }
    var data = {
        'inviteMessage': inputValue,
        'questionId': question_id
    }
    console.log(JSON.stringify(data));
    $.ajax({
        type: "GET",
        dataType:"json",
        url: "/inviteMessage",
        data: data,
        success:function(response){
            console.log(response);
            if(response.code == 200){
                alert("发送成功！")
            }else{
                alert("发送失败！")
            }
        }
    });

}