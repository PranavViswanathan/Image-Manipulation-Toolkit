## Command Reference

### Load and Save Operations

Load an image into the application:
```
load <image-path> <image-name>
```
Example:
```
load images/koala.jpg koala
```

Save a processed image:
```
save <image-path> <image-name>
```
Example:
```
save images/koala-processed.jpg koala-processed
```

### RGB Operations

Split an image into RGB channels:
```
rgb-split <image-name> <red-image-name> <green-image-name> <blue-image-name>
```
Example:
```
rgb-split koala koala-red koala-green koala-blue
```

Combine RGB channels into a single image:
```
rgb-combine <red-image> <green-image> <blue-image> <combined-image>
```
Example:
```
rgb-combine koala-red koala-green koala-blue koala-combined
```

Extract red component:
```
red-component <image-name> <dest-image-name>
```
Example:
```
red-component koala koala-red
```

Extract green component:
```
green-component <image-name> <dest-image-name>
```
Example:
```
green-component koala koala-green
```

Extract blue component:
```
blue-component <image-name> <dest-image-name>
```
Example:
```
blue-component koala koala-blue
```

### Image Transformations

Flip image vertically:
```
vertical-flip <image-name> <dest-image-name>
```
Example:
```
vertical-flip koala koala-vert
```

Flip image horizontally:
```
horizontal-flip <image-name> <dest-image-name>
```
Example:
```
horizontal-flip koala koala-horiz
```

Adjust brightness:
```
brighten <image-name> <dest-image-name> <increment>
```
Example:
```
brighten koala koala-bright 10
```

### Effects and Filters

Apply blur effect:
```
blur <image-name> <dest-image-name>
```
Example:
```
blur koala koala-blurred
```

Apply split-view blur effect:
```
blur <image-name> <dest-image-name> split <percentage>
```
Example:
```
blur koala koala-split-blur split 50
```

Apply sepia tone:
```
sepia <image-name> <dest-image-name>
```
Example:
```
sepia koala koala-sepia
```

Sharpen image:
```
sharpen <image-name> <dest-image-name>
```
Example:
```
sharpen koala koala-sharp
```

### Advanced Operations

Compress image:
```
compress <percentage> <image-name> <dest-image-name>
```
Example:
```
compress 50 koala koala-compressed
```

Generate histogram:
```
histogram <image-name> <dest-image-name>
```
Example:
```
histogram koala koala-histogram
```

Auto color correction:
```
color-correct <image-name> <dest-image-name>
```
Example:
```
color-correct koala koala-corrected
```

### Masking Operations

Blur with masking
```
blur <image-name> <destination-name> mask <mask-Image-name>
```

Sharpening with masking
```
sharpen <image-name> <destination-name> mask <mask-Image-name>
```

Spepia with masking
```
sepia <image-name> <destination-name> mask <mask-Image-name>
```

Greyscale with masking
```
grayscale <image-name> <destination-name> mask <mask-Image-name>
```

Red componenet with masking
```
red-component <image-name> <destination-name> mask <mask-Image-name>
```

Blue componenet with masking
```
blue-component <image-name> <destination-name> mask <mask-Image-name>
```

Green componenet with masking
```
green-component <image-name> <destination-name> mask <mask-Image-name>
```

### System Commands

Execute a script file:
```
script <script-path>
```
Example:
```
script commands.txt
```

Exit the application:
```
exit
```
