// 智能替换
let id = $('#post-content')

let current_html = id.html()
    .replace(/，/g, " , ") //中文逗号 -> 英文逗号
    .replace(/\^(.+?)\^/g, function (string, first) { // ^内容^ -> <sup>内容</sup>
        // "^99^" 最长四个字符
        if (first.length>4)
            return string
        else return "<sup>" + (first) + "</sup>";
    })
;

id.html(current_html)


