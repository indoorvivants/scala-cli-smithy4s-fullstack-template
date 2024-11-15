docker:
	docker build . -t my-fullstack-scala:latest

export WATCHER_SCRIPT
watch-smithy4s:
	cd shared && \
		echo "$$WATCHER_SCRIPT" | scala-cli run _.sc -- watch

smithy4s:
	cd shared && \
		echo "$$WATCHER_SCRIPT" | scala-cli run _.sc -- generate && \
		scala-cli --power compile . -O -rewrite -O -source -O 3.4-migration

setup-ide:
	rm -rf .scala-build .bsp .metals 
	cd shared && scala-cli --power setup-ide .
	cd frontend && scala-cli --power setup-ide .
	cd backend && scala-cli --power setup-ide .

code-check:
	cd backend && scala-cli --power fmt . --check
	cd frontend && scala-cli --power fmt . --check

pre-ci:
	cd backend && scala-cli --power fmt .
	cd frontend && scala-cli --power fmt .

run-backend:
	cd backend && scala-cli --power run -w . --restart -- 9999

run-frontend:
	cd frontend && npm install && npm run dev


define WATCHER_SCRIPT

//> using scala 3.5.2
//> using dep "com.disneystreaming.smithy4s:smithy4s-codegen-cli_2.13:0.18.25"
//> using dep com.lihaoyi::os-lib::0.11.3
//> using dep com.lihaoyi::os-lib-watch::0.11.3

import scala.util.*
import util.chaining.*

import smithy4s.codegen.cli.CodegenCommand

var cache = Map.empty[os.Path, os.StatInfo]

def collect(paths: Seq[os.Path]): Map[os.Path, os.StatInfo] = 
	paths.map(path => path -> os.stat(path)).toMap


def regenerate(paths: Set[os.Path]) = 
		if paths.exists(_.ext == "smithy") then 
			val files = 
				os.walk(os.pwd).filter(_.ext == "smithy")

			val args = 
				files.map(_.toString) ++ 
				List("--skip", "resource", "--skip", "openapi") ++ 
				List("--output", (os.pwd / "generated").toString)

			val incoming = collect(files)
			if incoming != cache then 
				val codegenArgs = CodegenCommand.command.parse(args).right.get.args

				try
					os.remove.all(os.pwd / "generated")
					smithy4s.codegen.Codegen.generateToDisk(codegenArgs)
					cache = incoming
					println("OK")
				catch case exc =>
					println("Failed to generate spec")
					println(exc.getMessage())
			else 
				println("Skipping generation")

val files = 
	os.walk(os.pwd).filter(_.ext == "smithy")

args.headOption match
	case Some("watch") => 
		regenerate(files.toSet)
		os.watch.watch(Seq(os.pwd), regenerate)
		while(true) do Thread.sleep(1000)
	case Some("generate") => 
		regenerate(files.toSet)
	case _ => 
		println("pass either watch or generate")
		sys.exit(1)

endef
