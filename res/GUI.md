# Image Processing Application GUI 

![GUI Screenshot](GUI.png)

**Components:**

 **Menu Bar:**
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
    * **Channel Histograms:** Displays histograms for the red, green, blue, and luma channels, allowing users to analyze the distribution of pixel values in each channel.
    * **Overall Histogram:** Displays a combined histogram for the entire image, providing a visual representation of pixel intensity distribution.

**User  Interaction:**
* Users can interact with the GUI through buttons and menus to perform various image processing tasks.
* The application provides visual feedback through previews and status updates to enhance user experience.

## **Using the UI:**


Below is a sample walkthrough of how a user would interact with the GUI.

1. **Opening the GUI**

- The user can open the GUI by navigating to the folder which contains the JAR file. The JAR file for the project is located in the ```res``` folder.
- When in the folder, run the JAR file by using the below command

``` java -jar <jarfilename.jar```

This will lead to a popup with the GUI of the application loaded.

2. **Using the GUI**
- First, we need to load an image.
  - Click on the load image button on the left-hand pane. This will open a window where you can choose which image you wish to load.
  - After selecting the image you will see the image that you loaded on the center pane along with its histogram on the right pane
 
- Performing operations on the image
   - Choose an option from the left pane, say sharpen for instance.
   - You will receive a popup indicating that the operation has been completed.
   - The image in the middle pane will now be updated with the new, sharpened image.
 
- Previewing Operations on the image
     - Say you want to preview what sepia would look like on the image.
     - click on the ```Sepia Preview``` button.
     - now click on the sepia button, a popup with a slider will appear, select the amount of preview you would like to see.
     - after clicking ok, you will be able to view a split view between the original and the operated image.
     - You can either apply or cancel the operation, in this case, lets assume we wish to apply the change.
     - The sepia filter will be applied to the whole image.
 
- Undoing an operation
     - Say you wish to undo an operation that you performed, such as the sepia application.
     - Click on the undo button, which will allow you to revert to the previous image state.
 
- Exiting the application
     - Click on the close button, you will be prompted to save the image if you wish to.
     - If you wish to save the image, it will ask you where to save the image.
     - If not, the application will close.
