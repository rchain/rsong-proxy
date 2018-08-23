RCHAIN=$(HOME)/projects/rchain


story1.rho.loaded: story1.rho immersion.rho.loaded
	rnode eval $<
	touch $@

immersion.rho.loaded: immersion.rho Mint.rho.loaded
	rnode eval $<
	touch $@

Mint.rho.loaded: $(RCHAIN)/casper/src/main/rholang/MakeMint.rho rnode.pid
	rnode eval $<
	rnode eval $(RCHAIN)/casper/src/main/rholang/NonNegativeNumber.rho
	touch $@

# this stuff doesn't really work
