docker:
	docker build . -t my-fullstack-scala:latest

smithy4s:
	cd shared && \
		rm -rf fullstack_scala/protocol && \
		cs launch smithy4s --contrib -- generate protocol.smithy --skip resource --skip openapi && \
		scala-cli compile . -O -rewrite -O -source -O 3.4-migration

setup-ide:
	rm -rf .scala-build .bsp .metals 
	cd shared && scala-cli setup-ide .
	cd frontend && scala-cli setup-ide .
	cd backend && scala-cli setup-ide .

code-check:
	cd backend && scala-cli fmt . --check
	cd frontend && scala-cli fmt . --check

pre-ci:
	cd backend && scala-cli fmt .
	cd frontend && scala-cli fmt .
