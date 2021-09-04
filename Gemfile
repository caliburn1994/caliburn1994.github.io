source "https://rubygems.org"

require 'json'
require 'open-uri'
versions = JSON.parse(open('https://pages.github.com/versions.json').read)
gem 'github-pages', '207', group: :jekyll_plugins

group :jekyll_plugins do
  gem "jekyll-paginate-v2",'3.0.0'
  gem "jekyll-feed",'0.13.0'
  gem 'jekyll-admin','0.10.2'
  gem "jekyll-last-modified-at"
end