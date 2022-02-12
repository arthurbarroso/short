(ns short.shared.backoffice.css
  (:require [short.users.ui.views.login :as login]
            [short.shared.backoffice.styles.products :as products-styles]
            [short.shared.backoffice.layout :as layout]
            [short.shared.ui.image-picker :as image-picker]
            [short.shared.ui.css :refer [ui-styles]]))

(defn mount-style
  [style-text]
  (let [head (or (.-head js/document)
                 (aget (.getElementsByTagName js/document "head") 0))
        style-el (doto (.createElement js/document "style")
                   (-> .-type (set! "text/css"))
                   (.appendChild (.createTextNode js/document style-text)))]
    (.appendChild head style-el)))

(defn mount-ui-styles []
  (let [styles (str "\n" ui-styles "\n" products-styles/product-view-styles
                    "\n" login/login-screen-css
                    "\n" image-picker/image-picker-css
                    "\n" layout/sidebar-css
                    "\n" layout/layout-css)]
    (mount-style styles)))
