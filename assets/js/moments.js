$(document).ready(function() {

    var show_per_page = 1;    //每页显示条数
    var number_of_items = $('.markdown_pagnation').children('.hoverable').size();
    var number_of_pages = Math.ceil(number_of_items / show_per_page);

    //最外层是pagination，包含操作以及样式
    $('.markdown_pagnation').append('<ul  class=pagination></div><input id=current_page type=hidden><input id=show_per_page type=hidden>');
    $('#current_page').val(0);
    $('#show_per_page').val(show_per_page);

    //左箭头 left arrow
    var navigation_html = '<li class="waves-effect"><a class="prev" onclick="previous()"><i class="material-icons">chevron_left</i></a></li>';

    var current_link = 0;
    while (number_of_pages > current_link) {
        navigation_html += '<li class="waves-effect"><a class="page" onclick="go_to_page(' + current_link + ')" longdesc="' + current_link + '">' + (current_link + 1) + '</a></li>';
        current_link++;
    }
    //右箭头 right arrow
    navigation_html += ' <li class="waves-effect"><a class="next" onclick="next()"><i class="material-icons">chevron_right</i></a></li>';

    $('.pagination').html(navigation_html);
    $('.pagination .page:first').addClass('active');

    $('.markdown_pagnation').children().css('display', 'none');
    $('.markdown_pagnation').children().slice(0, show_per_page).css('display', 'block');

});



function go_to_page(page_num) {
    var show_per_page = parseInt($('#show_per_page').val(), 0);

    start_from = page_num * show_per_page;

    end_on = start_from + show_per_page;

    $('.markdown_pagnation').children().css('display', 'none').slice(start_from, end_on).css('display', 'block');

    $('.page[longdesc=' + page_num + ']').addClass('active').siblings('.active').removeClass('active');

    $('#current_page').val(page_num);
}


//前一页
function previous() {

    new_page = parseInt($('#current_page').val(), 0) - 1;
    //if there is an item before the current active link run the function
    if ($('.active').prev('.page').length == true) {
        go_to_page(new_page);
    }

}

function next() {
    new_page = parseInt($('#current_page').val(), 0) + 1;
    //if there is an item after the current active link run the function
    if ($('.active').next('.page').length == true) {
        go_to_page(new_page);
    }

}