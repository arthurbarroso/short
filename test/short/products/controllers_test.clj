(ns short.products.controllers-test
  (:require [short.products.controllers :as cont]
            [test-helpers :as th]
            [clojure.test :refer [deftest testing is use-fixtures]]))

(defonce database-conn (atom nil))

(use-fixtures :once
  (fn [f]
    (reset! database-conn (th/create-in-memory-db
                           "products-tests-controllers"))
    (f)))

(def p {:sku "fasjfsaoij123skulegal"
        :active true
        :slug "some-slug-sku-legal"
        :price 30
        :quantity 2})

(deftest products-create-product-controller-test
  (testing "Creates a new product"
    (let [database database-conn
          {:keys [status body]}
          ((cont/create-product-controller! @database)
           {:parameters {:body p}})]
      (is (= 201 status))
      (is (= (:sku p) (:product/sku body)))))
  (testing "Fails to create a new product when the SKU is already in use"
    (let [database database-conn
          {:keys [status _body]}
          ((cont/create-product-controller! @database)
           {:parameters {:body p}})]
      (is (= 400 status)))))
