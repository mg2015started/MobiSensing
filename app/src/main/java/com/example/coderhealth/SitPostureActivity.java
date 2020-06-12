package com.example.coderhealth;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Pose;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SitPostureActivity extends AppCompatActivity {

    private static final String TAG = SitPostureActivity.class.getSimpleName();

    private static final double MIN_OPENGL_VERSION = 3.0;

    private FaceArFragment arFragment;
    private ModelRenderable faceRegionsRenderable;
    private Texture faceMeshTexture;
    TextView mTv, mTv2;
    AnchorNode lastAnchorNode;
    private final HashMap<AugmentedFace, AugmentedFaceNode> faceNodeMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sit_posture);
        arFragment = (FaceArFragment) getSupportFragmentManager().findFragmentById(R.id.face_fragment);

        mTv = findViewById(R.id.mTv);
        mTv2 = findViewById(R.id.mTv2);
        ModelRenderable.builder()
                .setSource(this, R.raw.fox_face)
                .build()
                .thenAccept(
                        modelRenderable -> {
                            faceRegionsRenderable = modelRenderable;
                            modelRenderable.setShadowCaster(false);
                            modelRenderable.setShadowReceiver(false);
                        });
        // Load the face mesh texture.
        Texture.builder()
                .setSource(this, R.drawable.fox_face_mesh_texture)
                .build()
                .thenAccept(texture -> faceMeshTexture = texture);

        ArSceneView sceneView = arFragment.getArSceneView();

//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                    addLineBetweenHits(hitResult, plane, motionEvent);
//                });


        sceneView.setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        Scene scene = sceneView.getScene();

        scene.addOnUpdateListener(
                (FrameTime frameTime) -> {
                    if (faceRegionsRenderable == null || faceMeshTexture == null) {
                        return;
                    }

                    Collection<AugmentedFace> faceList =
                            sceneView.getSession().getAllTrackables(AugmentedFace.class);
                    // Make new AugmentedFaceNodes for any new faces.
                    for (AugmentedFace face : faceList) {
                        if (!faceNodeMap.containsKey(face)) {
                            AugmentedFaceNode faceNode = new AugmentedFaceNode(face);

                            faceNode.setParent(scene);
                            faceNode.setFaceRegionsRenderable(faceRegionsRenderable);
                            faceNode.setFaceMeshTexture(faceMeshTexture);
                            faceNodeMap.put(face, faceNode);
                        }
                    }

                    // Remove any AugmentedFaceNodes associated with an AugmentedFace that stopped tracking.
                    Iterator<Map.Entry<AugmentedFace, AugmentedFaceNode>> iter =
                            faceNodeMap.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<AugmentedFace, AugmentedFaceNode> entry = iter.next();
                        AugmentedFace face = entry.getKey();


                        Pose left = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_LEFT);
                        Pose right = face.getRegionPose(AugmentedFace.RegionType.FOREHEAD_RIGHT);
//              face.getm
                        // AugmentedFace node
//              face.createAnchor(left);
//              face.createAnchor(right);
                        float lx = left.tx();
                        float ly = left.ty();
                        float lz = left.tz();
                        float rx = right.tx();
                        float ry = right.ty();
                        float rz = right.tz();
                        double llength = Math.sqrt(lx * lx + ly * ly + lz * lz);
                        double rlength = Math.sqrt(rx * rx + ry * ry + rz * rz);
                        BigDecimal b1 = new BigDecimal(llength);
                        BigDecimal r1 = new BigDecimal(rlength);

                        double spec = b1.add(r1).divide(new BigDecimal("2")).multiply(new BigDecimal("100")).floatValue();
                        Log.d("wzz","-----" + llength + "----" + rlength);
                        Log.d("wzz","-----" + b1.add(r1).divide(new BigDecimal("2")));
                        DecimalFormat decimalFormat = new DecimalFormat();
                        Log.d("wzz","-----" + decimalFormat.format((b1.add(r1).divide(new BigDecimal("2")))) + "m");
                        mTv.setText("到屏幕距离： " + decimalFormat.format(spec) + "cm");

                        if (spec < 50) {
                            mTv2.setText(Html.fromHtml("距离<font color='#ff0000'>过近</font>!"));
                        }
                        else {
                            mTv2.setText("距离合适");
                        }


                        if (face.getTrackingState() == TrackingState.STOPPED) {
                            Log.d("wzz","进入模式");
//                            drawLine(face.createAnchor(left),face.createAnchor(right));
                            AugmentedFaceNode  faceNode = entry.getValue();
                            faceNode.setParent(null);
                            iter.remove();
                        }
                    }
                });
    }

//    private void addLineBetweenHits(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
//        Anchor anchor = hitResult.createAnchor();
//        AnchorNode anchorNode = new AnchorNode(anchor);
//
//        if (lastAnchorNode != null) {
//            anchorNode.setParent(arFragment.getArSceneView().getScene());
//            Vector3 point1, point2;
//            point1 = lastAnchorNode.getWorldPosition();
//            point2 = anchorNode.getWorldPosition();
//
//        /*
//            First, find the vector extending between the two points and define a look rotation
//            in terms of this Vector.
//        */
//            final Vector3 difference = Vector3.subtract(point1, point2);
//            final Vector3 directionFromTopToBottom = difference.normalized();
//            final Quaternion rotationFromAToB =
//                    Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
//            MaterialFactory.makeOpaqueWithColor(getApplicationContext(), color.black)
//                    .thenAccept(
//                            material -> {
///* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
//       to extend to the necessary length.  */
//                                ModelRenderable model = ShapeFactory.makeCube(
//                                        new Vector3(.01f, .01f, difference.length()),
//                                        Vector3.zero(), material);
///* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
//       the midpoint between the given points . */
//                                Node node = new Node();
//                                node.setParent(anchorNode);
//                                node.setRenderable(model);
//                                node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
//                                node.setWorldRotation(rotationFromAToB);
//                            }
//                    );
//            lastAnchorNode = anchorNode;
//        }

//    private void drawLine(Anchor a, Anchor b) {
//        AnchorNode a1 = new AnchorNode(a);
//        AnchorNode b1 = new AnchorNode(b);
//
//        a1.setParent(arFragment.getArSceneView().getScene());
//        b1.setParent(arFragment.getArSceneView().getScene());
//
//        Vector3 point1, point2;
//        point1 = a1.getWorldPosition();
//        point2 = b1.getWorldPosition();
//
//        final Vector3 difference = Vector3.subtract(point1, point2);
//        final Vector3 directionFromTopToBottom = difference.normalized();
//        final Quaternion rotationFromAToB =
//                Quaternion.lookRotation(directionFromTopToBottom, Vector3.up());
//        MaterialFactory.makeOpaqueWithColor(getApplicationContext(), new Color(0, 255, 244))
//                .thenAccept(
//                        material -> {
//                        /* Then, create a rectangular prism, using ShapeFactory.makeCube() and use the difference vector
//                               to extend to the necessary length.  */
//                            ModelRenderable model = ShapeFactory.makeCube(
//                                    new Vector3(.01f, .01f, difference.length()),
//                                    Vector3.zero(), material);
//                        /* Last, set the world rotation of the node to the rotation calculated earlier and set the world position to
//                               the midpoint between the given points . */
//                            Node node = new Node();
//                            node.setParent(a1);
//                            node.setRenderable(model);
//                            node.setWorldPosition(Vector3.add(point1, point2).scaled(.5f));
//                            node.setWorldRotation(rotationFromAToB);
//                        }
//                );
//    }


}

