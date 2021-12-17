(ns day14
  (:require [clojure.string :as s]))

(def example-input (slurp "day14input_eg.txt"))

(defn rules-lookup [input]
  (into {}
        (map (fn [line]
               (let [[k v] (s/split line #" -> ")]
                 [(seq k) (first (seq v))]))
        (s/split input #"\n"))))

(defn expand [current lookup]
  (conj (vec (interleave current
                     (mapcat lookup (partition 2 1 current))))
        (last current)))

(defn part1 [input]
  (let [[init-str rules-str] (s/split input #"\n\n")
        init-seq             (vec (seq init-str))
        lookup               (rules-lookup rules-str)
        final-seq            (reduce
                               (fn [curr-seq _i]
                                 (expand curr-seq lookup))
                               init-seq
                               (range 10))
        freq-counts          (sort (map val (frequencies final-seq)))]
    (- (last freq-counts) (first freq-counts))))

(comment
  (part1 example-input)
  (part1 (slurp "day14input.txt")))

(defn part2 [input]
  (let [[init-str rules-str] (s/split input #"\n\n")
        init-seq             (vec (seq init-str))
        lookup               (rules-lookup rules-str)
        final-seq            (reduce
                               (fn [curr-seq _i]
                                 (expand curr-seq lookup))
                               init-seq
                               (range 10))
        freq-counts          (sort (map val (frequencies final-seq)))]
    (- (last freq-counts) (first freq-counts))))

(defn add-or-init [x y]
  (if x (+ x y) y))

(defn freq-from [[left right] lookup depth]
  (if (= depth 1)
    (-> {}
        (update left add-or-init 1)
        (update right add-or-init 1)
        (update (lookup [left right]) add-or-init 1))
    (let [left-freq  (freq-from [left (lookup [left right])] lookup (dec depth))
          right-freq (freq-from [(lookup [left right]) right] lookup (dec depth))]
      (reduce
        (fn [tot-freq [k v]]
          (update tot-freq k add-or-init v))
        left-freq
        right-freq))))

(comment
  (let [[init-str rules-str] (s/split example-input #"\n\n")
        init-seq             (vec (seq init-str))
        lookup               (rules-lookup rules-str)]
    (freq-from (take 2 init-seq) lookup 2)))
