// 智能替换
let id = $('#post-content')

let current_html = id.html().replace(/，/g, " , ");

id.html(current_html)


