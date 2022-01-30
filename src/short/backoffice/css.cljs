(ns short.backoffice.css
  (:require [short.ui.css :refer [ui-styles]]
            [short.backoffice.styles.products :as products-styles]
            [short.users.client.views.login :as login]
            [short.backoffice.components.image-picker :as image-picker]
            [short.backoffice.layout :as layout]))

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
