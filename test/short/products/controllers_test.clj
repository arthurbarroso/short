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

(defn p [opts]
  (merge
   {:sku "fasjfsaoij123skulegal"
    :active true
    :slug "some-slug-sku-legal"
    :price 30
    :title "test-title"
    :quantity 2}
   opts))

(deftest products-create-product-controller-test
  (testing "Creates a new product"
    (let [database database-conn
          {:keys [status body]}
          ((cont/create-product-controller! @database)
           {:parameters {:body (p {:sku "cool-sku-test-wont-fail-bruh"
                                   :slug "some-slug-that-wont-fail-bruh"})}})]
      (is (= 201 status))
      (is (= "cool-sku-test-wont-fail-bruh" (:product/sku body)))))
  (testing "Fails to create a new product when the SKU is already in use"
    (let [database database-conn
          _ ((cont/create-product-controller! @database)
             {:parameters {:body (p {:sku "fail-please"
                                     :slug "fail-ffs"})}})
          {:keys [status _body]}
          ((cont/create-product-controller! @database)
           {:parameters {:body (p {:sku "fail-please"
                                   :slug "fail-ffs"})}})]
      (is (= 400 status)))))

(deftest get-product-by-slug-controller-test
  (testing "Finds a product by it's slug"
    (let [database database-conn
          data (p {:sku "some-test-random-cool"
                   :slug "get-find-slug"})
          _
          ((cont/create-product-controller! @database)
           {:parameters {:body data}})
          {:keys [status body]}
          ((cont/get-product-by-slug-controller! @database)
           {:parameters {:path {:product (:slug data)}}})]
      (is (= 200 status))
      (is (= (:slug data) (:product/slug body)))))
  (testing "Fails to find a product using a non-existent slug"
    (let [database database-conn
          {:keys [status _body]}
          ((cont/get-product-by-slug-controller! @database)
           {:parameters {:path {:product "randooooooooooooom-non-existent"}}})]
      (is (= 404 status)))))

(deftest render-product-by-slug-controller-test
  (testing "Finds and renders a product by it's slug"
    (let [database database-conn
          data (p {:sku "some-test-random-cool-render"
                   :slug "get-find-slug-render"})
          _
          ((cont/create-product-controller! @database)
           {:parameters {:body data}})
          {:keys [status _body cookies]}
          ((cont/render-product-by-slug-controller! @database)
           {:parameters {:path {:product (:slug data)}}})
          parsed-cookie (m/decode "application/json" (:value (get cookies "product")))]
      (is (= "get-find-slug-render" (:product/slug parsed-cookie)))
      (is (= 200 status))))
  (testing "Fails to find and render a product using a non-existent slug"
    (let [database database-conn
          {:keys [status _body]}
          ((cont/render-product-by-slug-controller! @database)
           {:parameters {:path {:product "randooooooooooooom-non-existent"}}})]
      (is (= 404 status)))))
