# meta-clang (a C language family frontend and LLVM compiler backend)

This layer provides [clang/llvm](http://clang.llvm.org/) as alternative to your system
C/C++ compiler for OpenEmbedded based distributions along with gcc

# Getting Started

```shell
git clone git://github.com/openembedded/openembedded-core.git
cd openembeeded-core
git clone git://github.com/openembedded/bitbake.git
git clone git://github.com/kraj/meta-clang.git

$ . ./oe-init-build-env
```

Edit conf/bblayers.conf to add meta-musl to layer mix e.g.

```python
BBLAYERS ?= " \
  /home/kraj/openembedded-core/meta-clang \
  /home/kraj/openembedded-core/meta \
  "
```

# Building

Below we build for qemuarm machine as an example

```shell
$ MACHINE=qemuarm bitbake core-image-minimal
```
# Running

```shell
$ runqemu qemuarm
```

# Limitations

Currently only few components are building with clang if you want to port/add more then please add
```Shell
TOOLCHAIN_pn-<recipe-name> = "clang"
DEPENDS_append_pn-<recipe-name> = " clang-cross "
```shell

to clang.conf

and OE will start using clang to cross compile that recipe

# Dependencies

```
URI: git://github.com/openembedded/openembedded-core.git
branch: master
revision: HEAD

URI: git://github.com/openembedded/bitbake.git
branch: master
revision: HEAD
```

Send pull requests to openembedded-devel@lists.openembedded.org with '[meta-clang]' in the subject'

When sending single patches, please use something like:

'git send-email -M -1 --to openembedded-devel@lists.openembedded.org --subject-prefix=meta-clang][PATCH'

You are encouraged to fork the mirror on [github](https://github.com/kraj/meta-clang/)
to share your patches, this is preferred for patch sets consisting of more than 
one patch. Other services like gitorious, repo.or.cz or self hosted setups are 
of course accepted as well, 'git fetch <remote>' works the same on all of them.
We recommend github because it is free, easy to use, has been proven to be reliable 
and has a really good web GUI.

Layer Maintainer: Khem Raj <raj.khem@gmail.com>
