source("rds-pop.R")
load("brightkite.rdata")

n <- nrow(loc)
id <- loc$id
idmap <- id
for(i in 1:length(id)) idmap[id[i]] <- i
el1 <- cbind(idmap[el[,1]], idmap[el[,2]])
el1 <- cbind(pmin(el1[,1],el1[,2]),pmax(el1[,1],el1[,2]))
el1 <- unique(el1)
el1 <- cbind(el1, 1)

nbrs1 <- lapply(1:n, function(i){if(i %% 100 == 1)print(i); c(el1[el1[,1]==i,2],el1[el1[,2]==i,1])})

ns <- 2000 # rds sample size
seeds <- 12 # #of seeds
g <- rep(1,n)
d <- sapply(nbrs1,length)

pest <- list()
for(i in 1:40){
  rds <- sampRDS(el1, d, seeds, g, ns, FALSE)
  pest[[i]] <- unlist(populationEstimate(rds$subject,rds$recruiter, d[rds$subject], nbrs1))
  print(pest[[i]])
}
pest <- do.call(rbind, pest)
pest


boxplot(pest,ylim=c(0,120000))
abline(h=n,col="red")


hashSize <- c(1000, 5000, 10000, 100000, 1000000)
rho <- 1 / hashSize
pest2 <- list()
for(i in 1:50){
  rds <- sampRDS(el1, d, seeds,g,ns,FALSE)
  pest2[[i]] <- sapply(hashSize, function(hs){
    hash <- floor(runif(n, min = 0, max=hs))
    unlist(populationEstimateHash(rds$subject,rds$recruiter, d[rds$subject], nbrs1, hash, 1 / hs))    
  })
  print(pest2[[i]])
}
pest_est <- t(sapply(pest2, function(x) x[1,]))
colnames(pest_est) <- paste0("P", hashSize)
pest_cross <- t(sapply(pest2, function(x) x[2,]))
colnames(pest_cross) <- paste0("C", hashSize)


boxplot(cbind(pest_est,pest_cross),ylim=c(0,120000), xlab="Hash Size",
        main=paste0("britekite n=",ns," seeds=",seeds," n_sim=",nrow(pest_est)," (P=Pooled, C=Cross Seed)"))
abline(h=n,col="red")
