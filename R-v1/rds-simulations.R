source("C:/Users/Bilal Khan/Dropbox/Current Papers/Telefunken/telefunken3/R/rds-pop.R")

n <- 10000 #pop size
ns <- 700 # rds sample size
seeds <- 7 # #of seeds
#d <- round(rlnorm(n,meanlog = 1)) + 1
d <- round(rexp(n,1/5)) + 1 #heavy tailed degrees
#d <- rpois(n,lambda = 5) + 1 #light tail
#d <- rep(10, n)
g <- rep(1,n)
el <- makeGraph(d)
nbrs <- lapply(1:n, function(i) c(el[el[,1]==i,2],el[el[,2]==i,1]))
pest <- list()
for(i in 1:20){
  
  rds <- sampRDS(el, d, seeds,g,ns,FALSE, pr = c(0,.1,.9))
  pest[[i]] <- unlist(populationEstimate(rds$subject,rds$recruiter, d[rds$subject], nbrs))
  print(pest[[i]])
}
pest <- do.call(rbind, pest)
pest


boxplot(pest,ylim=c(0,20000))
abline(h=n,col="red")


hashSize <- c(16000) # 500, 1000, 5000, 10000, 100000)
rho <- 1 / hashSize
pest2 <- list()
for(i in 1:10){
  rds <- sampRDS(el, d, seeds,g,ns,FALSE)
  pest2[[i]] <- sapply(hashSize, function(hs){
    hash <- floor(runif(n, min = 0, max=hs))
    unlist(populationEstimateHash(rds$subject,rds$recruiter, d[rds$subject], nbrs, hash, 1 / hs))    
  })
  print(pest2[[i]])
}
pest_est <- t(sapply(pest2, function(x) x[1,]))
colnames(pest_est) <- paste0("P", hashSize)
pest_cross <- t(sapply(pest2, function(x) x[2,]))
colnames(pest_cross) <- paste0("C", hashSize)


boxplot(cbind(pest_est,pest_cross),ylim=c(0,20000), xlab="Hash Size",
        main=paste0("Configuration n=",ns," seeds=",seeds," n_sim=",nrow(pest_est)," (P=Pooled, C=Cross Seed)"))
abline(h=n,col="red")
