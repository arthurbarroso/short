(ns short.admin.views.login
  (:require [short.admin.template :as template]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.form :as form]
            [short.ui.section :as section]
            [short.ui.label :as label]))

(defn login-view []
  [template/layout
   ^{:key "login"}
   [section/section
    [form/form
     [:<>
      [:h2
       "Log in to the admin panel"]
      [label/label
       "E-mail address"]
      [input/input {:type "email"
                    :placeholder "e-mail address"
                    :on-change #()}]
      [label/label
       "Password"]
      [input/input {:type "password"
                    :placeholder "password"
                    :on-change #()}]

      [button/button {:text "Log in"
                      :extra-styles {:width "100%"
                                     :margin-top "3%"}}]]]]])
