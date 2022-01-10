(ns day1-performance)

(def measurements (vec (take 10000000 (repeatedly rand))))

(->> (range 1 (count measurements))
     (map (fn [i] (if (> (measurements i) (measurements (dec i)))
                    1 0)))
     (reduce +)
     time)

(->> (range 1 (count measurements))
     (transduce
       (map (fn [i] (if (> (measurements i) (measurements (dec i)))
                      1 0)))
       +)
     time)

(time
  (loop [i   1
         acc 0]
    (if (= i (count measurements))
      acc
      (recur (inc i)
             (+ acc (if (> (measurements i) (measurements (dec i)))
                      1 0))))))

(time
  (let [n (count measurements)]
    (loop [i   1
           acc 0]
      (if (= i n)
        acc
        (recur (inc i)
               (+ acc (if (> (measurements i) (measurements (dec i)))
                        1 0)))))))

(time
  (let [n (count measurements)]
    (loop [i    1
           acc  0
           left (first measurements)]
      (if (= i n)
        acc
        (recur (inc i)
               (if (> (measurements i) left)
                 (inc acc)
                 acc)
               (measurements i))))))

(time
  (let [n (count measurements)]
    (loop [i    1
           acc  0
           left (first measurements)]
      (if (= i n)
        acc
        (let [right (measurements i)]
          (recur (inc i)
                 (if (> right left)
                   (inc acc)
                   acc)
                 right))))))
