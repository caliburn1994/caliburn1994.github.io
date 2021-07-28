//import marked from  "marked.min"

$(function() {
    $('figure.highlight').each(function (index, entry) {
        if ($(entry).find('table').length < 1) {
            $(entry).find('pre').css('padding', '5px');
        }
    });

    // 为TOC添加标题
    add_TOC_title()

    update_tag_a();
    update_footnotes();

    // abbr的modal模块
    add_modal('#post-content')

    // 添加折叠代码功能
    collapse_codes_blocks()

    // 为 h2 h3 h4 增加数字
    insert_Headings()

    // 代码块的标签
    build_code_block()
});


function add_modal(afterId) {
    $("abbr").each(function (index, entry) {
        let parentAttr = $(entry).parent().attr("href");
        let href;
        // if href is not exist, the element of that tag is not one we want.
        if (parentAttr) {
            href = parentAttr.replace("#", "")
        } else {
            $(entry).contents().unwrap()
            return
        }

        //add modal
        const html = '<div id="' + href + '" class="modal" style="width:fit-content; width:-webkit-fit-content; width:-moz-fit-content;max-width:50%">' +
            '<div class="modal-content">' + marked($(entry).attr("title")) + " </div>" +
            "</div>";
        $(html).insertAfter(afterId)

        // css
        $(entry.parentElement).html($(entry).text())
            .css("border-bottom", "1px dotted #000")
            .css("text-decoration", "none")
    })

    window.$('.modal').modal();
}

function add_TOC_title() {
    const toc = $('#toc_container')
    toc.html(
        '<details open><summary class="toc_title">目录</summary>' + toc.html() + '</details>'
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
function collapse_codes_blocks() {
    $('.kyakya_collap').each(function (index, entry) {
        // console.info('Summary:' + $(entry).next().html())
        if (!$(entry).text().trim()) {
            $(entry).text("【详情】：")
        }

        // console.info(`Content:` + $(entry).next().html())
        $(entry).html(
            "<details>" +
                "<summary>" + $(entry).html() + "</summary>"
                + $(entry).next().html() +
            "</details>"
        )

        $(entry).next().remove()
    });
}

// <h2>heading</h2> ==> <h2>1 heading</h2>
// insert Auto Numbering for Headings
// ref :https://stackoverflow.com/questions/5127017/automatic-numbering-of-headings-h1-h6-using-jquery
function insert_Headings() {
    let indices = [];

    // jQuery will give all the HNs in document order
    $('h2,h3,h4,h5,h6').each(function (index, entry) {
        const hIndex = parseInt(entry.nodeName.substring(1)) - 2;

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
        $(entry).prepend(indices.join(".") + ". ");
    });
}


// add a id and name to code blocks
function build_code_block() {
    let next_is_code_block = false;
    let tabs_node = null;
    $(".highlighter-rouge").each(function (index, entry) {

        // if the code block is plaintext, do nothing
        const class_name = $(entry).attr("class")
        if (~class_name.indexOf("language-plaintext")) {
            return true;
        }
        // set a name and id for code blocks
        $(entry).attr("name", "code-block-" + index);
        $(entry).attr("id", "code-block-" + index);

        const code_lang = class_name.split(" ")[0].split("-")[1];

        // build tags
        if (next_is_code_block === false) { // build header
            $(entry).before('<ul class="tabs" style="padding-left: 0;line-height: 20px;height: 20px"></ul>');
            tabs_node = $(entry).prev();
        }
        tabs_node.append('<li class="tab" style="line-height: 20px;height: 20px" ><a href="#code-block-' + index + '" class="">' + code_lang + '</a></li>');  // build the content
        next_is_code_block = false; //reset
        const next_node_class_name = $(entry).next().attr("class") + "";
        if (~next_node_class_name.indexOf("highlighter-rouge")) {
            next_is_code_block = true;
        }
    });

    $('.tabs').tabs();
}

// 点击外部链接将会开启新的tab
function update_tag_a(value = '_blank') {
    $('a')
        .not('[target]')
        .not('[href^="#"]')  // #开头 不进行处理
        .each((index, entry) => {
            $(entry).attr('target', value);
        })
}

function update_footnotes() {
    $('a.footnote')
        .each((index, entry) => {
            $(entry).text('[' + $(entry).text() + ']')
        })
}