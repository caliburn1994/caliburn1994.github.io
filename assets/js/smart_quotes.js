// 智能替换
let id = $('#post-content')

let current_html = id.html()
    .replace(/，/g, " , ")
    .replace(/\^(.+?)\^/g, function (string, first) {
        // string is the full result of the regex "[2]"
        //first is the number 2 from "draft [2]"
        return "<sup>" + (first) + "</sup>";
    })
;

id.html(current_html)


