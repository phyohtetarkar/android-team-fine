package com.team.androidfine.ui.member;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.team.androidfine.R;
import com.team.androidfine.databinding.MemberEditFragmentBinding;
import com.team.androidfine.ui.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class MemberEditFragment extends Fragment {

    static final String KEY_MEMBER_ID = "member_id";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSION_TAKE_PHOTO = 2;
    static final int REQUEST_PICK_IMAGE = 3;
    static final int PERMISSION_WRITE_STORAGE = 4;

    private String currentPhotoFilePath;
    private boolean saveExit;

    private MemberEditViewModel viewModel;
    private MemberEditFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setTitle("Edit Member");
        activity.switchToggle(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProviders.of(this).get(MemberEditViewModel.class);
        viewModel.saveResult.observe(this, result -> {
            if (result) {
                saveExit = true;
                Navigation.findNavController(getView()).popBackStack();
            } else {
                Toast.makeText(requireContext(), "Error occurred.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MemberEditFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        binding.setVm(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnDelete = view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            viewModel.delete();
        });

        AppCompatImageView imageView = view.findViewById(R.id.memberPhoto);
        imageView.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_STORAGE);
                return;
            }

            showBottomSheetDialog();
        });
    }

    private void showBottomSheetDialog() {

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());

        View bottomSheetView = getLayoutInflater().inflate(R.layout.layout_take_picture_action, null);
        LinearLayout layoutTakePhoto = bottomSheetView.findViewById(R.id.layoutTakePhoto);
        LinearLayout layoutChooseGallery = bottomSheetView.findViewById(R.id.layoutChooseGallery);

        layoutTakePhoto.setOnClickListener(tv -> {
            dialog.dismiss();
            dispatchTakePictureIntent();
        });

        layoutChooseGallery.setOnClickListener(tv -> {
            dialog.dismiss();
            dispatchChoosePictureIntent();
        });

        dialog.setContentView(bottomSheetView);
        dialog.show();
    }

    private void dispatchChoosePictureIntent() {

        Intent contentIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentIntent.setType("image/*");

        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(contentIntent, "Choose Image Browser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickImageIntent});

        startActivityForResult(chooserIntent, REQUEST_PICK_IMAGE);

    }

    private void dispatchTakePictureIntent() {

        //check permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_TAKE_PHOTO);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(), "com.team.androidfine.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String photoFile = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(photoFile, ".jpg", storageDir);

        currentPhotoFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(getView()).navigateUp();
                return true;
            case R.id.action_save:
                viewModel.save();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        int id = args != null ? args.getInt(KEY_MEMBER_ID, 0) : 0;

        viewModel.findMember(id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && currentPhotoFilePath != null) {
            binding.memberPhoto.setImageURI(Uri.parse(currentPhotoFilePath));
            viewModel.member.getValue().setPhoto(currentPhotoFilePath);
        } else if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            writeImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_TAKE_PHOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        } else if (requestCode == PERMISSION_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showBottomSheetDialog();
            }
        }
    }

    private void writeImage(Uri uri) {

        try {
            File file = createImageFile();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(uri), null, options);

            final int REQUEST_SIZE = 240;
            int tmp_width = options.outWidth;
            int tmp_height = options.outHeight;

            int scale = 1;
            while (tmp_width / 2 >= REQUEST_SIZE && tmp_height / 2 >= REQUEST_SIZE) {
                tmp_width /= 2;
                tmp_height /= 2;
                scale *= 2;
            }

            BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
            scaleOptions.inSampleSize = scale;

            Bitmap bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(uri), null, scaleOptions);

            ExifInterface exifInterface = new ExifInterface(getPath(uri));
            int rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationDegree = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0) {
                matrix.preRotate(rotationDegree);
            }

            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));

            binding.memberPhoto.setImageBitmap(rotatedBitmap);
            viewModel.setOldPhoto(viewModel.member.getValue().getPhoto());
            viewModel.member.getValue().setPhoto(currentPhotoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int exifToDegrees(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private String getPath(Uri uri) {

        String[] projections = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(uri, projections, null, null, null);
        if (cursor == null) {
            return null;
        }

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();

        return s;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!saveExit && currentPhotoFilePath != null) {
            File file = new File(currentPhotoFilePath);
            if (file.exists()) file.delete();
        }
        MainActivity activity = (MainActivity) requireActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.switchToggle(true);
        activity.hideKeyboard();
    }
}
