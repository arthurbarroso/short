(ns short.backoffice.views.panel
  (:require [short.backoffice.template :as template]
            [short.ui.form :as form]
            [short.ui.section :as section]))

(defn panel-view []
  (fn []
    [template/layout
     ^{:panel "login"}
     [section/section
      [form/form
       [:<>
        [:h2
         "Authenticated"]]]]]))
