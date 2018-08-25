# To start the node, I use:
# rnode run -s --no-upnp & echo $! >rnode.pid && sleep 5
#
# Then to load all the contracts:
# make
#
# To wipe out a buggy contract and try again:
# kill $(cat rnode.pid) && rm -rf ~/.rnode/rspace/ && rm -f *.loaded
# make

RCHAIN=$(HOME)/projects/rchain


story1.rho.loaded: story1.rho immersion.rho.loaded
	rnode eval $<
	touch $@

immersion.rho.loaded: ../../../main/resources/rho/immersion.rho Mint.rho.loaded
	rnode eval $<
	touch $@

Mint.rho.loaded: $(RCHAIN)/casper/src/main/rholang/MakeMint.rho rnode.pid
	rnode eval $<
	rnode eval $(RCHAIN)/casper/src/main/rholang/NonNegativeNumber.rho
	touch $@
