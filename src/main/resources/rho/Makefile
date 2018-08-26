# To start the node, I use:
# rnode run -s --no-upnp & echo $! >rnode.pid && sleep 5
#
# Then to load all the contracts:
# make
#
# To wipe out a buggy contract and try again:
# kill $(cat rnode.pid) && rm -rf ~/.rnode/rspace/ && rm -f *.loaded
# make


user.rho.loaded: new_user.rho immersion.rho.loaded
	rnode eval $<
	touch $@

immersion.rho.loaded: immersion.rho Mint.rho.loaded
	rnode eval $<
	touch $@

Mint.rho.loaded: $(MAKE_MINT_RHO) rnode.pid
	rnode eval $<
	rnode eval $(NON_NEGATIVE_NUMBER_RHO)
	touch $@
