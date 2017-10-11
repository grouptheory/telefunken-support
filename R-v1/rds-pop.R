# make config geaph
makeGraph <- function(d){
  n <- length(d)
  edges <- list()
  d1 <- d
  while(sum(d1)>0.5){
    l <- sample.int(n,1,prob=d1)
    dt <- d1
    dt[l] <- d1[l] - 1
    if(sum(dt) > 0.5){
      k <- sample.int(n,1,prob=dt)
      v <- TRUE
      d1[k] <- d1[k] - 1
    }else{
      k <- sample.int(n,1,prob=d)
      v <- FALSE
    }
    d1[l] <- d1[l] - 1
    edges[[length(edges)+1]] <- c(l,k,v)
  }
  el <- do.call(rbind,edges)
  el
}

#rds sampler
sampRDS <- function(el, d, ns, g, ss, biased=TRUE,pr = c(0,.1,.9)){
  maxR <- length(pr) -1
  n <- length(g)
  ml <- if(is.factor(g)) max(levels(g)) else max(g)
  if(biased){
    seeds <- sample.int(n,ns,prob=as.numeric(as.factor(g))-1)
  }else{
    seeds <- sample.int(n,ns, prob = d)
  }
  samp <- seeds
  recr <- rep(-1,ns)
  time <- 0 + (1:ns)/10000000
  rcTime <- rexp(ns)
  v <- rep(-1,ns)
  t1 <- time
  while(length(samp) < ss){
    subjIndex <- which.min(t1 + rcTime)
    if(length(subjIndex)==0){
      print("redraw")
      subj <- sample( (1:n)[-samp],1)
      samp <- c(samp,subj)
      recr <- c(recr,-1)
      time <- c(time,max(time+1))
      rcTime <- c(rcTime,rexp(1))
      t1 <- c(t1,max(time+1))
      v <- c(v,-1)
    }else{
      t <- t1[subjIndex] + rcTime[subjIndex]
      t1[subjIndex] <- NA
      subj <- samp[subjIndex]
      nr <- sample(0:maxR,1,replace=FALSE,prob=pr)
      nbrs <- rbind(el[el[,1]==subj,2:3,drop=FALSE],
                    el[el[,2]==subj & el[,3]>0.5,c(1,3),drop=FALSE]
      )
      nbrs <- nbrs[!(nbrs[,1] %in% samp) & nbrs[,1]!=subj,,drop=FALSE]
      nr <- min(nr,nrow(nbrs))
      if(nr>0){
        s <- sample.int(nrow(nbrs),nr,replace=FALSE)
        s <- s[!duplicated(nbrs[s,1])]
        nr <- length(s)
        samp <- c(samp,nbrs[s,1])
        recr <- c(recr,rep(subj,nr))
        tm <- t + t + (0:(nr-1)) / 1000000
        time <- c(time,tm)
        t1 <- c(t1,tm)
        rcTime <- c(rcTime,rexp(nr))
        v <- c(v,nbrs[s,2])
      }
    }
  }
  data.frame(subject=samp,recruiter=recr,time=time,v=v)
}

#get the root seed for each subject
getSeed <- function(id, recruiter.id){
  sid <- -1
  get.seed <- function(i, history) {
    row <- match(i, id)
    rec.id <- recruiter.id[row]
    if(rec.id==i){
      stop(sprintf("Yikes! The data says that the person with id %s recruited themselves! Please check that the coupon information in the data for that person is correct :-)",i),call.=FALSE)}
    if(rec.id %in% history){
      stop("Loop found in recruitment tree.")
    }
    if (rec.id == sid) {
      return(i)
    }
    else {
      get.seed(rec.id,history=c(history,i))
    }
  }
  seed <- sapply(id, get.seed,history=c())
  seed
}



populationEstimate <- function(subject, recruiter, degree, nbrs){
  ns <- length(subject)
  outSet <- do.call(c, apply(cbind(subject,recruiter), 1, function(x){
    excl <- c(x[2], subject[recruiter == x[1]])
    nb <- nbrs[[x[1]]]
    for(e in excl){
      i <- which(nb %in% e)
      if(length(i) > 0)
        nb <- nb[-i[1]]
    }
    nb
  }))
  m <- length(outSet[outSet %in% subject])
  dSample <- mean(degree) - 1
  dPopulation <- ns / sum(1/degree)
  #browser()
  result <- list(
    estimate=(dSample / dPopulation) * ns * length(outSet) / m ,
    noDegreeEstimate=ns * length(outSet) / m
  )
  seed <- getSeed(subject,recruiter)
  seedIds <- unique(seed)
  crossSeedEstimateNum <- 0
  crossSeedEstimateDenom <- 0
  for(s in seedIds){
    outSet <- do.call(c, as.list(apply(cbind(subject[seed == s],recruiter[seed == s]), 1, function(x){
      excl <- c(x[2], subject[recruiter == x[1]])
      nb <- nbrs[[x[1]]]
      for(e in excl){
        i <- which(nb %in% e)
        if(length(i) > 0)
          nb <- nb[-i[1]]
      }
      nb
    })))
    if(is.null(outSet))
      next
    m <- length(outSet[outSet %in% subject[seed != s]])
    dSample <- mean(degree[seed != s]) - 1
    dPopulation <- ns / sum(1/degree)
    crossSeedEstimateNum <- crossSeedEstimateNum + length(outSet) * 
      length(subject[seed != s]) * dSample / dPopulation
    crossSeedEstimateDenom <- crossSeedEstimateDenom + m
  }
  #print(crossSeedEstimateNum)
  #print(crossSeedEstimateDenom)
  result$crossSeedEstimate <- crossSeedEstimateNum / crossSeedEstimateDenom
  result
}



populationEstimateHash <- function(subject, recruiter, degree, nbrs, hash, rho){
  ns <- length(subject)
  outSet <- apply(cbind(subject,recruiter), 1, function(x){
    excl <- c(hash[x[2]], hash[subject[recruiter == x[1]]])
    nb <- hash[nbrs[[x[1]]]]
    for(e in excl){
      i <- which(nb %in% e)
      if(length(i) > 0)
        nb <- nb[-i[1]]
    }
    nb
  })
  #browser()
  dSample <- mean(degree) - 1
  dPopulation <- ns / sum(1/degree)
  matchSet <- lapply(outSet, function(x) x[x %in% hash[subject]])

  NTilda <- function(N){
    wts <-  lapply(matchSet, function(x){
      wts1 <- list()
      for(a in x){
        mId <- which(hash[subject] == a)
        for(id in mId){
          wts1[[length(wts1) + 1]] <- 1 / (dPopulation * rho * (N - 1) / (degree[id] - 1) + 1)
        }
      }
      unlist(wts1)
    })
    m <- sum(unlist(wts))
    (dSample / dPopulation) * ns * length(unlist(outSet)) / m
  }
  opt <- uniroot(function(N) NTilda(N) - N, interval=c(2, 7000000000))
  
  seed <- getSeed(subject,recruiter)
  seedIds <- unique(seed)  
  NTildaCross <- function(N){
    crossSeedEstimateNum <- 0
    crossSeedEstimateDenom <- 0
    for(s in seedIds){
      outSet <- apply(cbind(subject[seed == s],recruiter[seed == s]), 1, function(x){
        excl <- c(hash[x[2]], hash[subject[recruiter == x[1]]])
        nb <- hash[nbrs[[x[1]]]]
        for(e in excl){
          i <- which(nb %in% e)
          if(length(i) > 0)
            nb <- nb[-i[1]]
        }
        nb
      })
      if(is.null(outSet))
        next
      matchSet <- lapply(outSet, function(x) x[x %in% hash[subject[seed != s]]])
      wts <-  lapply(matchSet, function(x){
        wts1 <- list()
        for(a in x){
          mId <- which(hash[subject] == a & seed != s)
          for(id in mId){
            wts1[[length(wts1) + 1]] <- 1 / (dPopulation * rho * (N - 1) / (degree[id] - 1) + 1)
          }
        }
        unlist(wts1)
      })
      m <- sum(unlist(wts))
      dSample <- mean(degree[seed != s]) - 1
      dPopulation <- ns / sum(1/degree)
      crossSeedEstimateNum <- crossSeedEstimateNum + length(unlist(outSet)) * 
        length(subject[seed != s]) * dSample / dPopulation
      crossSeedEstimateDenom <- crossSeedEstimateDenom + m
      #print(length(unlist(outSet)) * 
      #        length(subject[seed != s]) * dSample / dPopulation / m)
    }
    crossSeedEstimateNum / crossSeedEstimateDenom
  }
  optCross <- uniroot(function(N) NTildaCross(N) - N, interval=c(2, 7000000000))
  result <- list(
    estimate=opt$root,
    crossSeedEstimate= optCross$root
  )
  result
}
