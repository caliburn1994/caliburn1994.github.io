// ==UserScript==
// @name        Github font changer
// @namespace   local.greasemonkey.githubfontchanger
// @include     https://github.com/*
// @version     1
// @grant       none
// ==/UserScript==

// 需求: GitHub对中文字体的支持不完全
var fontdef ="Noto Sans ! important"; // Set your font here.

// Function helper to inject css
function addGlobalStyle(css) {
    var head, style;
    head = document.getElementsByTagName('head')[0];
    if (!head) { return; }
    style = document.createElement('style');
    style.type = 'text/css';
    style.innerHTML = css;
    head.appendChild(style);


    var fontLoader = function (param) {
        var link = document.createElement('link');
        link.type = 'text/css';
        link.rel = 'stylesheet';

        //link.href = 'http://fonts.googleapis.com/css?family=Oswald&effect=neon';
        document.head.appendChild(link);

        link.href = 'http://fonts.googleapis.com/css?family=' + param.family
    };
    fontLoader({
        family: 'Noto Sans',
    });
    window.document.body.style.cssText = 'font-family:Noto Sans !important';
}

// Apply the font-family definition to code styles.
addGlobalStyle(
    '.blob-code { font-family: ' + fontdef + '; } ' +
    '.blob-num { font-family: ' + fontdef + '; } ' +
    '');