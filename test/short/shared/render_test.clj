(ns short.shared.render-test
  (:require [short.shared.render :as r]
            [clojure.test :refer [deftest testing is]]))

(deftest render-add-component-string-to-html-test
  (testing "Adds the rendered component string to the html"
    (let [base-html "<body>${{html-string}}</body>"
          res (r/add-component-string-to-html
               base-html "oi")]
      (is (= "<body>oi</body>" res)))))

(deftest render-add-scripts-to-include-to-html-test
  (testing "Adds the scripts strings to the html"
    (let [base-html "<body>${{scripts-to-include}}</body>"
          res (r/add-scripts-to-include-to-html
               base-html "script")]
      (is (= "<body>script</body>" res)))))

(deftest render-add-hydrate-script-fn-to-html-test
  (testing "Adds the hydrate script to the html"
    (let [base-html "<body>${{hydrate-script}}</body>"
          res (r/add-hydrate-script-fn-to-html
               base-html "script")]
      (is (= "<body>script</body>" res)))))

(deftest render-add-title-to-html-test
  (testing "Adds the title to the html"
    (let [base-html "<body>${{title}}</body>"
          res (r/add-title-to-html
               base-html "title")]
      (is (= "<body>title</body>" res)))))

(deftest render-add-stylesheets-to-html-test
  (testing "Adds the needed stylesheets to the html"
    (let [base-html "<body>${{stylesheets}}</body>"
          res (r/add-stylesheets-to-html
               base-html "style")]
      (is (= "<body>style</body>" res)))))

(deftest render-html-template-test
  (testing "Renders the html adding all the needed fields to it"
    (let [opts {:pre-rendered-html "component"
                :hydrate-script-fn "hydrate"
                :scripts-to-include "scripts"
                :title "title"
                :stylesheets "stylesheets"}
          res (r/html-template opts)]
      (is (= res
             "<html>\n      <head>\n        <meta charset=\"utf-8\">\n        stylesheets\n        scripts\n        <title>title</title>\n      </head>\n      <body>\n        <div id=\"root\">component</div>\n      </body>\n      <script>hydrate</script>\n    </html>")))))
