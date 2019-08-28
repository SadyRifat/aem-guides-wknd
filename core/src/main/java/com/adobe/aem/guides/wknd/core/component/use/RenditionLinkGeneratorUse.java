package com.adobe.aem.guides.wknd.core.component.use;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.Iterator;
import java.util.regex.Matcher;

public class RenditionLinkGeneratorUse extends WCMUsePojo{

    public static final String PROP_IMAGE_SOURCE = "fileReference";
    private ResourceResolver resourceResolver;
    private String renditionsPath = "";

    @Override
    public void activate() throws Exception {
        String imagePath =  getResource().getValueMap().get(PROP_IMAGE_SOURCE, StringUtils.EMPTY);
        imagePath += "/jcr:content/renditions";
        int imageWidth = 0;
        int display = 0;

        Resource resource = getResourceResolver().getResource(imagePath);
        if(resource != null){
            Iterator<Resource> listChildren = resource.listChildren();
            while (listChildren.hasNext()){
                Resource renderImage = listChildren.next();
                String renderImageName = renderImage.getName();

                if(renderImageName.compareTo("original") != 1){
                    imageWidth = generateImageWidth(renderImageName);

                    if(imageWidth <= 500){
                        display = 1;
                    } else if(imageWidth>500 && imageWidth <980){
                        display = 2;
                    } else {
                        display = 3;
                    }
                }

                renditionsPath += imagePath + "/" + renderImageName + " " + display + "x,";
            }
        }

        if (renditionsPath.length() > 0) {
            renditionsPath = renditionsPath.substring(0, renditionsPath.length() - 1);
        }
    }

    public int generateImageWidth(String imageName){
        String[] arrOfStr = imageName.split("\\.", 10);
        int width = 0;
        for (String str : arrOfStr){
            if(StringUtils.isNumeric(str)){
                width = Integer.parseInt(str);
                break;
            }
        }
        return width;
    }

    public String getRenditionsPath(){return renditionsPath;}
}