package org.example;

import com.github.sarxos.webcam.Webcam;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.example.grpc.GreetingServiceGrpc;
import org.example.grpc.GreetingServiceOuterClass;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    private static final String filePath = "/image/";
    private static final String fileFullPath = "C:/Users/Hyp/IdeaProject/gRPC/grpc-server/src/main/resources/image/";

    @Override
    public void serverResponse(GreetingServiceOuterClass.GrpcRequest request,
                               StreamObserver<GreetingServiceOuterClass.GrpcResponse> responseObserver) {

        String command = request.getCommand();
        System.out.println(request);
        int numberOfShots = 5;
        if (command.equals("start")) {
            for (int i = 0; i < numberOfShots; i++) {
                getCompressingImage(i);
            }
            for (int i = 0; i < numberOfShots; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream inputStream = getClass().getResourceAsStream(filePath + i + ".jpg");
                    byte[] bytes = inputStream.readAllBytes();
                    GreetingServiceOuterClass.GrpcResponse response = GreetingServiceOuterClass.GrpcResponse.newBuilder()
                            .setTemperatureSensor(i + 70).setImageWeb(ByteString.copyFrom(bytes)).build();
                    responseObserver.onNext(response);
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        responseObserver.onCompleted();
    }


    private static void getCompressingImage(int i) {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        BufferedImage input = webcam.getImage();
        ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(0.5f);
        try {
            imageWriter.setOutput(new FileImageOutputStream(
                    new File(fileFullPath + i+ ".jpg")));
            IIOImage image = new IIOImage(input, null, null);
            imageWriter.write(null, image, imageWriteParam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        webcam.close();
    }


}
