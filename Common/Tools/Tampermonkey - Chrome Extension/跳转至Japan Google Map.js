// ==UserScript==
// @name         Go to Japan Google Map
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        https://www.google.com/maps/*
// @exclude      https://www.google.com/maps/*hl=ja*
// @icon         https://www.google.com/s2/favicons?sz=64&domain=google.com
// @grant        none
// ==/UserScript==

(function() {
console.log('Redirecting Google Map')
var url = new URL(window.location.href)
url.searchParams.set('hl', 'ja');
window.location.replace(url.href);
})();