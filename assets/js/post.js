//import marked from  "marked.min"

$(document).ready(function () {
    $('figure.highlight').each(function (x, r) {
        if ($(r).find('table').length < 1) {
            $(r).find('pre').css('padding', '5px');
        }
    });


    //abbr的modal模块
    addModal('#post-content')

    addModalListener()


    //为Toc添加标题
    addTitleForTOC()

    //添加折叠代码功能
    collapseCodesBlock()
});



function addModal(afterId) {

    jQuery("abbr").each(function () {
        var href = this.parentElement.getAttribute("href").replace("#", "")
        var content = this.getAttribute("title")

        //add modal
        var result = "<div id=\"" + href + "\" " +
            "class=\"modal\" style='width:fit-content; width:-webkit-fit-content; width:-moz-fit-content;max-width:50%'>\n " +
            "  <div class=\"modal-content\">\n" + marked(content)  + "\n " +
            " </div>\n" +
            "</div>"
        $(result).insertAfter(afterId)


        //修改样式
        $(this.parentElement).html(
            $(this).text()
        )
            .css("border-bottom", "1px dotted #000")
            .css("text-decoration", "none")
    })

    //remove abbr

};

function addModalListener() {
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.modal');
        var instances = M.Modal.init(elems, options);
    });

    // Or with jQuery

    $(document).ready(function () {
        $('.modal').modal();
    });

}


function addTitleForTOC() {

    const toc=$('#toc_container')

    toc.html(
        '<p class="toc_title">目录</p>' + toc.html()
    )
}


//collapse codes block
function collapseCodesBlock(){


    $('.cxj_code').each(function() {

        if($(this).text() === '')$(this).text("详情")


        $(this).html(
            "<details>" +
            "<summary>" +
            $(this).html()+
            "</summary>" +
            $(this).next().html()+
            "</details>"
        )


        $(this).next().remove()
    });


}