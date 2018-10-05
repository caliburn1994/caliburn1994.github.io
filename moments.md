---
title: "Moments 动态"
layout: moments
css: ["post.css","moments.css"]
js: ["post.js","moments.js"]
---
<div class="row">
  <div class="col s8 offset-s2 markdown_pagnation">
    <div class="markdown_item" markdown="1">
        {% include moments.md %}
    </div>
    <ul  class=pagination></div>
    <input id=current_page type=hidden>
    <input id=show_per_page type=hidden>
  </div>
</div>