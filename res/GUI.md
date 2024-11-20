## Image Processing Application GUI 

![GUI Screenshot](GUI.png)

**Components:**

* **Menu Bar:**
    * **Main:**
        * **Load:** Loads an image into the application for processing.
        * **Save:** Saves the currently processed image to a file.
        * **Undo:** Reverts the last image processing operation.
        * **Revert to Origin:** Reverts the image back to its original state. 

* **Image Processing Tools:**
    * **Extract Red:** Extracts the red channel from the image.
    * **Extract Green:** Extracts the green channel from the image.
    * **Extract Blue:** Extracts the blue channel from the image.
    * **Extract Luma:** Extracts the luma (brightness) component of the image.
    * **Extract Intensity:** Extracts the intensity (brightness) of each pixel in the image.
    * **Extract Value:** Extracts the value component of the image.
    * **Flip Horizontal:** Flips the image horizontally.
    * **Flip Vertical:** Flips the image vertically.
    * **Downscale:** Reduces the size of the image.
    * **Adjust Brightness:** Adjusts the brightness of the image.
    * **Blur:** Applies a blurring effect to the image.
        * **Blur Preview:** Displays a preview of the blurring effect before applying it. 
    * **Sharpen:** Sharpens the image.
        * **Sharpen Preview:** Displays a preview of the sharpening effect before applying it.
    * **Sepia:** Applies a sepia tone to the image.
        * **Sepia Preview:** Displays a preview of the sepia tone effect before applying it.
    * **Greyscale:** Converts the image to greyscale.
        * **Greyscale Preview:** Displays a preview of the greyscale effect before applying it.
    * **Color Correct:** Performs color correction on the image.
        * **Color Correct Preview:** Displays a preview of the color correction effect before applying it.
    * **Adjust Levels:** Adjusts the brightness, contrast, and gamma levels of the image.
        * **Adjust Levels Preview:** Displays a preview of the levels adjustment effect before applying it.

* **Image Display Area:**
    * **Main Image View:** Displays the currently loaded and processed image. 

* **Histogram View:**
    * **Channel Histograms:** Displays histograms for the red, green, blue , and luma channels, allowing users to analyze the distribution of pixel values in each channel.
    * **Overall Histogram:** Displays a combined histogram for the entire image, providing a visual representation of pixel intensity distribution.

**User  Interaction:**
* Users can interact with the GUI through buttons and menus to perform various image processing tasks.
* The application provides visual feedback through previews and status updates to enhance user experience.

**Conclusion:**
This GUI is designed to facilitate easy and intuitive image processing, allowing users to manipulate images effectively with a variety of tools and options.
