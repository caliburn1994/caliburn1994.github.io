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

    // 为h2 h3 h4 增加数字
    insertAutHoeading()

    // 代码块的标签
    build_code_block()
});


function addModal(afterId) {


    jQuery("abbr").each(function () {

        var parentAttr = $(this).parent().attr("href")
        console.log('parentAttr:' + parentAttr)
        var href;

        // if href is not exist, the element of that tag is not one we want.
        if (parentAttr) {
            href = parentAttr.replace("#", "")
        } else {
            $(this).contents().unwrap()
            return
        }


        var content = this.getAttribute("title")

        //add modal
        var result = "<div id=\"" + href + "\" " +
            "class=\"modal\" style='width:fit-content; width:-webkit-fit-content; width:-moz-fit-content;max-width:50%'>\n " +
            "  <div class=\"modal-content\">\n" + marked(content) + "\n " +
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

}

function addModalListener() {
    document.addEventListener('DOMContentLoaded', function () {
        var elems = document.querySelectorAll('.modal');
        M.Modal.init(elems, options);
    });

    // Or with jQuery

    $(document).ready(function () {
        window.$('.modal').modal();
    });

}


function addTitleForTOC() {

    const toc = $('#toc_container')

    toc.html(
        '<details><summary class="toc_title">目录</summary>' + toc.html() + '</details>'
    )
}

// exampe:
// <div class="kyakya_collap" value="example code："/>
// <pre>
// your code
// </pre>
// ==>>
//
// <div class="kyakya_collap" value="example code：">
//      <summary>example code:</summary>
//      <pre>
//         your code
//      </pre>
// </div>
function collapseCodesBlock() {
    $('.kyakya_collap').each(function () {
        if ($(this).attr('value') === '') {
            $(this).text("详情")
        } else {
            $(this).text($(this).attr('value'))
        }

        $(this).html(
            "<details>" +
            "<summary><u style='text-decoration-style: wavy;'>" +
            $(this).html() +
            "</u></summary>" +
            $(this).next().html() +
            "</details>"
        )
        $(this).next().remove()
    });
}

// <h2>heading</h2> ==> <h2>1 heading</h2>
// insert Auto Numbering for Headings
// ref :https://stackoverflow.com/questions/5127017/automatic-numbering-of-headings-h1-h6-using-jquery
function insertAutHoeading() {
    var indices = [];

    function addIndex() {
        // jQuery will give all the HNs in document order
        jQuery('h2,h3,h4,h5,h6').each(function (i, e) {
            const hIndex = parseInt(this.nodeName.substring(1)) - 2;

            // just found a levelUp event
            if (indices.length - 1 > hIndex) {
                indices = indices.slice(0, hIndex + 1);
            }

            // just found a levelDown event
            if (indices[hIndex] === undefined) {
                indices[hIndex] = 0;
            }

            // count + 1 at current level
            indices[hIndex]++;

            // display the full position in the hierarchy
            jQuery(this).prepend(indices.join(".") + ". ");

        });
    }

    jQuery(document).ready(function () {
        addIndex();
    });
}

function build_code_block() {
    build_tags();
    $('.tabs').tabs();

}

// add a id and name to code blocks
function build_tags() {

    let next_is_code_block = false;
    let tabs_node = null;
    $(".highlighter-rouge").each(function (i, e) {

        // if the code block is plaintext, do nothing
        const class_name = $(this).attr("class")
        if (~class_name.indexOf("language-plaintext")) {
            return true;
        }
        // set a name and id for code blocks
        $(this).attr("name", "code-block-" + i);
        $(this).attr("id", "code-block-" + i);

        const code_lang = class_name.split(" ")[0].split("-")[1];

        // build tags
        if (next_is_code_block === false) { // build header
            $(this).before('<ul class="tabs" style="padding-left: 0;height: auto;"></ul>');
            tabs_node = $(this).prev();
        }
        tabs_node.append('<li class="tab" style="height: 15px;"><a href="#code-block-' + i + '" class="">' + code_lang + '</a></li>');  // build the content
        next_is_code_block = false; //reset
        const next_node_class_name = $(this).next().attr("class") + "";
        if (~next_node_class_name.indexOf("highlighter-rouge")) {
            next_is_code_block = true;
        }
    });

    $('.tabs').tabs();
}
