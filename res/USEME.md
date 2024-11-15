# Imageinator Usage Examples

This document provides a set of example workflows and use cases to help you get started with the Imageinator image processing tool.

## Table of Contents
- [Basic Image Manipulation](#basic-image-manipulation)
- [RGB Channel Operations](#rgb-channel-operations)
- [Image Transformations](#image-transformations)
- [Applying Effects](#applying-effects)
- [Advanced Features](#advanced-features)
- [Batch Processing with Scripts](#batch-processing-with-scripts)

## Basic Image Manipulation

### Load and Save Images
Load an image:
```
load images/input.jpg input
```

Save a processed image:
```
save images/output.jpg processed-image
```

## RGB Channel Operations

### Split RGB Channels
Split an image into its RGB components:
```
rgb-split input input-red input-green input-blue
```

### Combine RGB Channels
Recombine the RGB channels into a single image:
```
rgb-combine input-red input-green input-blue combined
```

### Extract Color Components
Extract individual color components:
```
red-component input input-red
green-component input input-green 
blue-component input input-blue
```

## Image Transformations

### Flip Images
Flip an image vertically:
```
vertical-flip input input-flipped-vert
```

Flip an image horizontally:
```
horizontal-flip input input-flipped-horiz
```

### Adjust Brightness
Brighten an image by a specified increment:
```
brighten input input-brighter 20
```

## Applying Effects

### Blur Images
Apply a blur effect:
```
blur input input-blurred
```

Apply a split-view blur effect:
```
blur input input-split-blur split 60
```

### Apply Sepia Tone
Convert an image to sepia:
```
sepia input input-sepia
```

### Sharpen Images
Sharpen an image:
```
sharpen input input-sharpened
```

## Advanced Features

### Compress Images
Compress an image by a specified percentage:
```
compress 80 input input-compressed
```

### Generate Histogram
Create a histogram visualization of an image:
```
histogram input input-histogram
```

### Auto Color Correction
Automatically correct the colors of an image:
```
color-correct input input-corrected
```

## Batch Processing with Scripts

Create a script file (e.g., `commands.txt`) with a series of commands:

```
load images/input.jpg input
rgb-split input input-red input-green input-blue
brighten input-red input-red-brighter 15
rgb-combine input-red-brighter input-green input-blue input-modified
blur input-modified output-blurred
save images/output.jpg output-blurred
```

Then, execute the script:

```
script commands.txt
```

This script will:
1. Load the input image
2. Split it into RGB channels
3. Brighten the red channel
4. Recombine the modified red channel with the original green and blue
5. Apply a blur effect
6. Save the final processed image

Feel free to experiment with these examples and create your own workflows to suit your image processing needs.

## Additional Resources

- Refer to the [Command Reference](COMMAND.md) for a complete list of available commands and their usage.
- Check the [Project Structure](DOCUMENTATION.md#project-structure) section for more details on the internal organization of the Imageinator application.
