source "https://rubygems.org"

require 'json'
require 'open-uri'
versions = JSON.parse(open('https://pages.github.com/versions.json').read)
gem 'github-pages', versions['github-pages'], group: :jekyll_plugins

group :jekyll_plugins do
  gem "jekyll-paginate-v2"
  gem "jekyll-feed"
  gem 'jekyll-admin'
end
gems: [jekyll-mermaid]
