# Docker Image for TUKE Thesis

## Using the Image



The best way to use the image is to create following alias for your environment:

```bash
$ alias mkthesis='docker container run --rm -it \
  --volume /path/to/your/thesis:/thesis \
  --user $(id --user):$(id --group) \
  --name mkthesis \
  bletvaska/thesis make'
```

Then you can use prepared `Makefile` for all the jobs you need when preparing your thesis. To get help about the available targets, you can simply type:

```bash
$ mkthesis
```


## Build Image Locally

You can build image locally with following command:

```bash
$ docker image build --tag thesis --file Dockerfile .
```
