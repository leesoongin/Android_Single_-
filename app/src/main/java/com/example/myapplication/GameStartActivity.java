package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameStartActivity extends AppCompatActivity implements View.OnClickListener{

        String FP_id,SP_id;

        Boolean isThreadEnd;//thread종료 flag

        TextView roundText; //라운드 표시
        int gameRound;//게임 라운드

        ProgressBar progBar;//타이머 바
        Thread timerThread;//타이머 스레드
        int sec;//타이머 시간
        Handler handler=new Handler();

        ImageView[] gameImage; // 게임 이미지 저장할곳
        ImageView FP_icon, SP_icon; //플레이어 아이콘 저장할 곳
        Drawable[] randomImage; //게임에 사용될 27장의 이미지 저장

        ImageButton backButton, hintButton; //맨위 버튼 두개

        ImageButton checkCombination, inputCombination; // 결 합 버튼

        Boolean[] isImageCheck;  // 눌림 확인 불리언.
        Boolean isFPturn, isSPturn; //1p 2p 눌림확인 불리언.
        int FP_gamePoint, SP_gamePoint;
        TextView FP_gameScore, SP_gameSocre;

        Boolean isBackgroundColor, isFigure, isFigureColor; //배경색, 도형모양 도형 색 모두 일치하는지 검사하기위한 불리언.
        int[] pressedImageNumber; //합을 위한 ,,  눌린 이미지번호 저장할 int
        int[] getPressedImageInfomation; //눌린 위치의 이미지 정보를 가지고 오게끔.
        static int imagePressedLimit;//이미지 3개이상 선택 못하게끔
        int[] randomNumber;

        ArrayList<ImageInfomationList> imageInfomationList = new ArrayList<ImageInfomationList>();

        ArrayList<String> registCombinationHistory = new ArrayList<String>();//매치히스토리 저장
        ArrayList<Integer> registAllCombination = new ArrayList<Integer>();//합의 모든 경우를 저장
        TextView CombinationResult;//매치히스토리 텍스트뷰
        String CombinationResultStr; //매치히스토리 저장할 문자열


        Boolean isCheckBackgroundColor, isCheckFigure, isCheckFigureColor; //결을위한 불리언
        Boolean[] isThreeCheck;// 123 321 같은 중복을 방지하기 위해서.
        Boolean isOverLap; //위의 경우에 true켜지는 불리언.

        TextView hintText;
        String combinationNumberList; //힌트에 사용하기 위함 모든 합의 경우의 수를 문자열로 저장할거임
        int combinationCount; // 경우의 수 가지 수

        Intent waitIntent;
        Intent GameEndIntent;//라운드 종료시 나올 화면 인텐트

        public MediaPlayer bgm,click,touchSound,answer_sound;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game_start);

            FP_id=getIntent().getStringExtra("1P_id"); //각각의 플레이어 아이디를 받아오자
            SP_id=getIntent().getStringExtra("2P_id"); //why? 게임 종료시 이걸로 데이터 저장할거니까.

            waitIntent=new Intent(this,GameWaitActivity.class); /**게임점수확인 중간엑티비티***/
            GameEndIntent=new Intent(this,GameEndActivity.class);

            gameImage = new ImageView[9];
            randomImage = new Drawable[27];
            randomNumber = new int[9];
            pressedImageNumber = new int[3];
            getPressedImageInfomation = new int[3];

            isImageCheck = new Boolean[9];

            isThreeCheck = new Boolean[3];

            isThreadEnd=false;

            bgm=MediaPlayer.create(this,R.raw.bgm);
            click=MediaPlayer.create(this,R.raw.click);
            touchSound=MediaPlayer.create(this,R.raw.stonesound);
            answer_sound=MediaPlayer.create(this,R.raw.answer_sound);
            //음악 시작 끝날때 까지.

            roundText = (TextView) findViewById(R.id.roundText);
            gameRound = 1;

            progBar=(ProgressBar)findViewById(R.id.progBar);
            progBar.setMax(60); //최대값 60으로 설정
            progBar.setProgress(60);

            backButton = (ImageButton) findViewById(R.id.backButton);
            checkCombination = (ImageButton) findViewById(R.id.checkCombination);
            inputCombination = (ImageButton) findViewById(R.id.inputCombination);

            hintButton = (ImageButton) findViewById(R.id.hintButton);
            hintText = (TextView) findViewById(R.id.hintText);

            gameImage[0] = (ImageView) findViewById(R.id.gameImage1);
            gameImage[1] = (ImageView) findViewById(R.id.gameImage2);
            gameImage[2] = (ImageView) findViewById(R.id.gameImage3);
            gameImage[3] = (ImageView) findViewById(R.id.gameImage4);
            gameImage[4] = (ImageView) findViewById(R.id.gameImage5);
            gameImage[5] = (ImageView) findViewById(R.id.gameImage6);
            gameImage[6] = (ImageView) findViewById(R.id.gameImage7);
            gameImage[7] = (ImageView) findViewById(R.id.gameImage8);
            gameImage[8] = (ImageView) findViewById(R.id.gameImage9);

            CombinationResult = (TextView) findViewById(R.id.CombinationResult);

            FP_gameScore = (TextView) findViewById(R.id.FP_gameScore);
            SP_gameSocre = (TextView) findViewById(R.id.SP_gameScore);

            FP_icon = (ImageView) findViewById(R.id.FP_icon);
            SP_icon = (ImageView) findViewById(R.id.SP_icon);

            hintText.setText("");

            combinationNumberList = "";//합의 경우의 수 가지 수
            combinationCount = 0;

            for (int i = 0; i < isImageCheck.length; i++) //불리언 초기화 완료.
                isImageCheck[i] = false;

            for (int i = 0; i < pressedImageNumber.length; i++)
                pressedImageNumber[i] = 0;


            isFPturn = false;
            isSPturn = false;
            isBackgroundColor = false;
            isFigure = false;
            isFigureColor = false; //합을 위한
            isCheckBackgroundColor = false;
            isCheckFigure = false;
            isCheckFigureColor = false; // 결을 위한
            isThreeCheck[0] = false;
            isThreeCheck[1] = false;
            isThreeCheck[2] = false; // 123 321 같은 중복을 방지하기 위해서.
            isOverLap = false;

            imagePressedLimit = 0;
            FP_gamePoint = 0;
            SP_gamePoint = 0;

            CombinationResult.setText("");
            CombinationResultStr = "";
            FP_gameScore.setText("0");
            SP_gameSocre.setText("0");

            registImageInfomationList();//이미지 정보 저장
            makeRandomNumber();//랜덤수 생성
            makeImage();//drawale 객체 생성 27개 이미지
            makeGameBoard(); //게임판 생성
            allCheckCombinationFunction();//결 검사

            addThread();//스레드 추가
            timerThread.start();//타이머 스레드 실행.

            Toast.makeText(this, "" + combinationCount, Toast.LENGTH_SHORT).show();

            backButton.setOnClickListener(this);
            hintButton.setOnClickListener(this);
            inputCombination.setOnClickListener(this);//합
            checkCombination.setOnClickListener(this);//결

            gameImage[0].setOnClickListener(this); //각각의 이미지들
            gameImage[1].setOnClickListener(this);
            gameImage[2].setOnClickListener(this);
            gameImage[3].setOnClickListener(this);
            gameImage[4].setOnClickListener(this);
            gameImage[5].setOnClickListener(this);
            gameImage[6].setOnClickListener(this);
            gameImage[7].setOnClickListener(this);
            gameImage[8].setOnClickListener(this);

            FP_icon.setOnClickListener(this); //1p 아이콘
            SP_icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v == inputCombination) {//합
                //터치사운드.
                click.start();

                if (isFPturn == false && isSPturn == false || isFPturn == true && isSPturn == true) {//1p 혹은 2p 아이콘이 눌려있어야한다.
                    Toast.makeText(this, "플레이어의 턴을 정해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    for (int i = 0; i < pressedImageNumber.length; i++) {
                        for (int j = 0; j < isImageCheck.length; j++) {//눌린 위치숫자
                            if (isImageCheck[j] == true) {
                                pressedImageNumber[i] = j + 1;
                                i++;
                            }
                        }//inner for
                    }//for

                    if (isFPturn == true) {
                        if (checkOverLapFunction() == false)//중복이 아니라면
                            checkCombinationFunction(); // 합 검사 함수
                        /////여기 들어오지 못했다면 어차피 false라 밑에 해당안됨.

                        if (isBackgroundColor == true && isFigure == true && isFigureColor == true) {
                            answer_sound.start();
                            FP_gamePoint += 1;
                            FP_gameScore.setText("" + FP_gamePoint);

                            CombinationResultStr += " " + pressedImageNumber[0] + pressedImageNumber[1] + pressedImageNumber[2] + "  ";

                            registCombinationHistory.add("" + pressedImageNumber[0] + pressedImageNumber[1] + pressedImageNumber[2]);//한번 입력된 조합 문자열로 저장.
                            CombinationResult.setText(CombinationResultStr);
                            isFPturn = false;//턴 바꾸는게 아니라 초기화시켜줌 밑에도 마찬가지.
                            FP_icon.setBackgroundResource(0);

                            if (combinationCount > 0)
                                combinationCount--;

                            isBackgroundColor = false;
                            isFigureColor = false;
                            isFigure = false;
                        }
                    } else if (isSPturn == true) {
                        if (checkOverLapFunction() == false)//중복이 아니라면
                            checkCombinationFunction(); // 합 검사 함수
                        /////여기 들어오지 못했다면 어차피 false라 밑에 해당안됨.

                        if (isBackgroundColor == true && isFigure == true && isFigureColor == true) {
                            answer_sound.start();
                            SP_gamePoint += 1;
                            SP_gameSocre.setText("" + SP_gamePoint);

                            CombinationResultStr += " " + pressedImageNumber[0] + pressedImageNumber[1] + pressedImageNumber[2] + "  ";

                            registCombinationHistory.add("" + pressedImageNumber[0] + pressedImageNumber[1] + pressedImageNumber[2]);//한번 입력된 조합 문자열로 저장
                            CombinationResult.setText(CombinationResultStr);
                            isSPturn = false;
                            SP_icon.setBackgroundResource(0);

                            if (combinationCount > 0)
                                combinationCount--;

                            isBackgroundColor = false;
                            isFigureColor = false;
                            isFigure = false;
                        }
                    }

                    for (int i = 0; i < isImageCheck.length; i++) {//화면에 눌린 표시없애줄친구
                        isImageCheck[i] = false;
                        gameImage[i].setBackgroundResource(0);
                        imagePressedLimit = 0;
                    }
                    if (isFPturn == true) { //합이 성립하지 않았을 경우에도 초기화시켜주기
                        FP_icon.setBackgroundResource(0);
                        isFPturn = false;
                    } else if (isSPturn == true) {
                        SP_icon.setBackgroundResource(0);
                        isSPturn = false;
                    }
                    Toast.makeText(this, "" + FP_gamePoint + SP_gamePoint, Toast.LENGTH_SHORT).show();
                }
            } else if (v == checkCombination) { // 결
                //터치사운드.
                click.start();
                if (isFPturn == false && isSPturn == false || isFPturn == true && isSPturn == true) {//1p 혹은 2p 아이콘이 눌려있어야한다.
                    Toast.makeText(this, "플레이어의 턴을 정해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (combinationCount == 0) { //주어진 합의 조합이 0 이 되었을떄.
                        makeRandomNumber();//랜덤수 생성
                        makeGameBoard();


                        //플레이어의 턴이 정해져 있다면
                        if (isFPturn == true) {
                            answer_sound.start();
                            FP_gamePoint += 3;
                            combinationNumberList = ""; //힌트 조합리스트 초기화
                            CombinationResultStr=""; //입력한 매치히스토리 초기화
                            CombinationResult.setText(CombinationResultStr);

                            registAllCombination.clear();//검사할때 들어갔던 모든합의 조합 초기화
                            registCombinationHistory.clear(); // 유저가 입력했던 합의 모든 조합 초기화
                            hintText.setText("");

                            isFPturn =false; // 턴 끝내고
                            FP_icon.setBackgroundResource(0);//백그라운드 이미지 다시 지우자

                            FP_gameScore.setText("" + FP_gamePoint);//게임점수 증가
                            roundText.setText(" " + (++gameRound) + " ROUND"); // 라운드 표시
                        } else if (isSPturn == true) {
                            answer_sound.start();
                            SP_gamePoint += 3;
                            combinationNumberList = "";//힌트 조합리스트 초기화
                            CombinationResultStr=""; //입력한 매치히스토리 초기화
                            CombinationResult.setText(CombinationResultStr);

                            registAllCombination.clear();//검사할때 들어갔던 모든합의 조합 초기화
                            registCombinationHistory.clear(); // 유저가 입력했던 합의 모든 조합 초기화
                            hintText.setText("");

                            isSPturn=false;
                            SP_icon.setBackgroundResource(0);

                            SP_gameSocre.setText("" + SP_gamePoint);
                            roundText.setText(" " + (++gameRound) + " ROUND"); // 라운드 표시
                        }
                        if(gameRound < 11){
                            sec=65;

                            waitIntent.putExtra("FP_score",FP_gamePoint);
                            waitIntent.putExtra("SP_score",SP_gamePoint);
                            waitIntent.putExtra("gameRound",gameRound);

                            startActivity(waitIntent);
                        }else if(gameRound == 11){
                            //게임종료화면으로 이동 새로만들기.
                            GameEndIntent.putExtra("1P_id",FP_id); //아이디
                            GameEndIntent.putExtra("2P_id",SP_id);
                            GameEndIntent.putExtra("FP_gameScore",FP_gamePoint); //점수
                            GameEndIntent.putExtra("SP_gameScore",SP_gamePoint);

                            sec =0;
                            startActivity(GameEndIntent);
                        }
                        allCheckCombinationFunction(); //생성된 게임판에 대한 결 검사
                    } else {//결 실패한다면
                        if (isFPturn == true) {
                            FP_gamePoint += -1;
                            FP_gameScore.setText("" + FP_gamePoint);
                        } else if (isSPturn == true) {
                            SP_gamePoint += -1;
                            SP_gameSocre.setText("" + SP_gamePoint);
                        }
                    }//else


                }//if

            } else if (v == gameImage[0]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[0] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[0].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[0] = true;
                    }
                } else if (isImageCheck[0] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[0].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[0] = false;
                    }
                }
            } else if (v == gameImage[1]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[1] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[1].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[1] = true;
                    }
                } else if (isImageCheck[1] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[1].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[1] = false;
                    }
                }
            } else if (v == gameImage[2]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[2] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[2].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[2] = true;
                    }
                } else if (isImageCheck[2] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[2].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[2] = false;
                    }
                }
            } else if (v == gameImage[3]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[3] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[3].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[3] = true;
                    }
                } else if (isImageCheck[3] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[3].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[3] = false;
                    }
                }
            } else if (v == gameImage[4]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[4] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[4].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[4] = true;
                    }
                } else if (isImageCheck[4] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[4].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[4] = false;
                    }
                }
            } else if (v == gameImage[5]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[5] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[5].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[5] = true;
                    }
                } else if (isImageCheck[5] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[5].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[5] = false;
                    }
                }
            } else if (v == gameImage[6]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[6] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[6].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[6] = true;
                    }
                } else if (isImageCheck[6] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[6].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[6] = false;
                    }
                }
            } else if (v == gameImage[7]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[7] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[7].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[7] = true;
                    }
                } else if (isImageCheck[7] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[7].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[7] = false;
                    }
                }
            } else if (v == gameImage[8]) {
                //터치사운드.
                touchSound.start();
                if (isImageCheck[8] == false) {
                    if (imagePressedLimit < 3) {
                        gameImage[8].setBackgroundResource(R.drawable.draw_yellow_line);


                        imagePressedLimit += 1;
                        isImageCheck[8] = true;
                    }
                } else if (isImageCheck[8] == true) {
                    if (imagePressedLimit <= 3) {
                        gameImage[8].setBackgroundResource(0);

                        imagePressedLimit -= 1;
                        isImageCheck[8] = false;
                    }
                }
            } else if (v == FP_icon) {
                //터치사운드.
                click.start();
                if (isFPturn == false) {
                    FP_icon.setBackgroundResource(R.drawable.draw_yellow_line);
                    isFPturn = true;
                } else if (isFPturn == true) {
                    FP_icon.setBackgroundResource(0);
                    isFPturn = false;
                }
            } else if (v == SP_icon) {
                //터치사운드.
                click.start();
                if (isSPturn == false) {
                    SP_icon.setBackgroundResource(R.drawable.draw_yellow_line);
                    isSPturn = true;
                } else if (isSPturn == true) {
                    SP_icon.setBackgroundResource(0);
                    isSPturn = false;
                }
            } else if (v == hintButton) {
                //터치사운드.
                click.start();
                hintText.setText("총 개수: " + combinationCount + "\n" + combinationNumberList);
            } else if (v == backButton) {
                //터치사운드.
                click.start();
                isThreadEnd=true;
                finish();
            }/////마지막조건문
        }//onCLick

        public void makeGameBoard() {//게임판 생성

            for (int i = 0; i < randomNumber.length; i++) { // 9 번
                for (int j = 0; j < randomImage.length; j++) { // 27 번
                    if ((randomNumber[i] - 1) == j)
                        gameImage[i].setImageDrawable(randomImage[j]);
                }//inner for
            }//for
        }

        public void makeImage() {
            randomImage[0] = getResources().getDrawable(R.drawable.cir1, null);
            randomImage[1] = getResources().getDrawable(R.drawable.cir2, null);
            randomImage[2] = getResources().getDrawable(R.drawable.cir3, null);
            randomImage[3] = getResources().getDrawable(R.drawable.cir4, null);
            randomImage[4] = getResources().getDrawable(R.drawable.cir5, null);
            randomImage[5] = getResources().getDrawable(R.drawable.cir6, null);
            randomImage[6] = getResources().getDrawable(R.drawable.cir7, null);
            randomImage[7] = getResources().getDrawable(R.drawable.cir8, null);
            randomImage[8] = getResources().getDrawable(R.drawable.cir9, null);

            randomImage[9] = getResources().getDrawable(R.drawable.rec1, null);
            randomImage[10] = getResources().getDrawable(R.drawable.rec2, null);
            randomImage[11] = getResources().getDrawable(R.drawable.rec3, null);
            randomImage[12] = getResources().getDrawable(R.drawable.rec4, null);
            randomImage[13] = getResources().getDrawable(R.drawable.rec5, null);
            randomImage[14] = getResources().getDrawable(R.drawable.rec6, null);
            randomImage[15] = getResources().getDrawable(R.drawable.rec7, null);
            randomImage[16] = getResources().getDrawable(R.drawable.rec8, null);
            randomImage[17] = getResources().getDrawable(R.drawable.rec9, null);

            randomImage[18] = getResources().getDrawable(R.drawable.tri1, null);
            randomImage[19] = getResources().getDrawable(R.drawable.tri2, null);
            randomImage[20] = getResources().getDrawable(R.drawable.tri3, null);
            randomImage[21] = getResources().getDrawable(R.drawable.tri4, null);
            randomImage[22] = getResources().getDrawable(R.drawable.tri5, null);
            randomImage[23] = getResources().getDrawable(R.drawable.tri6, null);
            randomImage[24] = getResources().getDrawable(R.drawable.tri7, null);
            randomImage[25] = getResources().getDrawable(R.drawable.tri8, null);
            randomImage[26] = getResources().getDrawable(R.drawable.tri9, null);
        }//makeImage

        public void registImageInfomationList() { //이미지 정보 저장
            //배경 가장밝은 1  중간 2 가장어둡 3 / 원 1 정사각형 2 세모 3 /도형색 검 1 민트 2 흰 3

            imageInfomationList.add(new ImageInfomationList(1, 1, 1)); //1
            imageInfomationList.add(new ImageInfomationList(1, 1, 2));
            imageInfomationList.add(new ImageInfomationList(1, 1, 3));
            imageInfomationList.add(new ImageInfomationList(2, 1, 1));
            imageInfomationList.add(new ImageInfomationList(2, 1, 2));
            imageInfomationList.add(new ImageInfomationList(2, 1, 3));
            imageInfomationList.add(new ImageInfomationList(3, 1, 1));
            imageInfomationList.add(new ImageInfomationList(3, 1, 2));
            imageInfomationList.add(new ImageInfomationList(3, 1, 3)); // 9 원
            imageInfomationList.add(new ImageInfomationList(1, 2, 1));
            imageInfomationList.add(new ImageInfomationList(1, 2, 2));
            imageInfomationList.add(new ImageInfomationList(1, 2, 3));
            imageInfomationList.add(new ImageInfomationList(2, 2, 1));
            imageInfomationList.add(new ImageInfomationList(2, 2, 2));
            imageInfomationList.add(new ImageInfomationList(2, 2, 3));
            imageInfomationList.add(new ImageInfomationList(3, 2, 1));
            imageInfomationList.add(new ImageInfomationList(3, 2, 2));
            imageInfomationList.add(new ImageInfomationList(3, 2, 3)); // 18 정사각형
            imageInfomationList.add(new ImageInfomationList(1, 3, 1));
            imageInfomationList.add(new ImageInfomationList(1, 3, 2));
            imageInfomationList.add(new ImageInfomationList(1, 3, 3));
            imageInfomationList.add(new ImageInfomationList(2, 3, 1));
            imageInfomationList.add(new ImageInfomationList(2, 3, 2));
            imageInfomationList.add(new ImageInfomationList(2, 3, 3));
            imageInfomationList.add(new ImageInfomationList(3, 3, 1));
            imageInfomationList.add(new ImageInfomationList(3, 3, 2));
            imageInfomationList.add(new ImageInfomationList(3, 3, 3)); // 27
        }

        public void makeRandomNumber() {
            for (int i = 0; i < randomNumber.length; i++) {
                randomNumber[i] = (int) (Math.random() * 27) + 1;
                for (int j = 0; j < i; j++) {//중복검사
                    if (randomNumber[i] == randomNumber[j]) {
                        i--;
                    }//if
                }//inner for
            }//for
        }//make random number

        public void checkCombinationFunction() { // 합 검사 함수   checkOverLapFunction 을 만들어서 추가하자 여기에
            if (pressedImageNumber[0] == 0 || pressedImageNumber[1] == 0 || pressedImageNumber[2] == 0) { //3개중 하나라도 눌리지 않은게있다면안됨
                Toast.makeText(this, "3개의 이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < getPressedImageInfomation.length; i++)
                    getPressedImageInfomation[i] = randomNumber[pressedImageNumber[i] - 1]; // 입력된 도형의 자리에 대한 이미지 정보를 저장하기.

                if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getBackGroundColor() == imageInfomationList.get(getPressedImageInfomation[1] - 1).getBackGroundColor()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getBackGroundColor() == imageInfomationList.get(getPressedImageInfomation[2] - 1).getBackGroundColor())
                    isBackgroundColor = true;
                else if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getBackGroundColor() != imageInfomationList.get(getPressedImageInfomation[1] - 1).getBackGroundColor()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getBackGroundColor() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getBackGroundColor()
                        && imageInfomationList.get(getPressedImageInfomation[1] - 1).getBackGroundColor() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getBackGroundColor())
                    isBackgroundColor = true;
                if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigure() == imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigure()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigure() == imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigure())
                    isFigure = true;
                else if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigure() != imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigure()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigure() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigure()
                        && imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigure() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigure())
                    isFigure = true;
                if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigureColor() == imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigureColor()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigureColor() == imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigureColor())
                    isFigureColor = true;
                else if (imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigureColor() != imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigureColor()
                        && imageInfomationList.get(getPressedImageInfomation[0] - 1).getFigureColor() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigureColor()
                        && imageInfomationList.get(getPressedImageInfomation[1] - 1).getFigureColor() != imageInfomationList.get(getPressedImageInfomation[2] - 1).getFigureColor())
                    isFigureColor = true;

                for (int i = 0; i < getPressedImageInfomation.length; i++)
                    getPressedImageInfomation[i] = 0;
            }

        }

        public void allCheckCombinationFunction() {
            for (int first = 0; first < 7; first++) {
                for (int second = 1; second < 8; second++) {
                    for (int third = 2; third < 9; third++) {
                        if (first != second && first != third && second != third) {//인덱스 서로 다 다르게
                            if (imageInfomationList.get(randomNumber[first] - 1).getBackGroundColor() == imageInfomationList.get(randomNumber[second] - 1).getBackGroundColor()
                                    && imageInfomationList.get(randomNumber[second] - 1).getBackGroundColor() == imageInfomationList.get(randomNumber[third] - 1).getBackGroundColor()) {
                                isCheckBackgroundColor = true;
                            }//모두같
                            else if (imageInfomationList.get(randomNumber[first] - 1).getBackGroundColor() != imageInfomationList.get(randomNumber[second] - 1).getBackGroundColor()
                                    && imageInfomationList.get(randomNumber[second] - 1).getBackGroundColor() != imageInfomationList.get(randomNumber[third] - 1).getBackGroundColor()
                                    && imageInfomationList.get(randomNumber[first] - 1).getBackGroundColor() != imageInfomationList.get(randomNumber[third] - 1).getBackGroundColor()) {
                                isCheckBackgroundColor = true;
                            }//모두 다른// background

                            if (imageInfomationList.get(randomNumber[first] - 1).getFigure() == imageInfomationList.get(randomNumber[second] - 1).getFigure()
                                    && imageInfomationList.get(randomNumber[second] - 1).getFigure() == imageInfomationList.get(randomNumber[third] - 1).getFigure()) {
                                isCheckFigure = true;
                            }//모두같
                            else if (imageInfomationList.get(randomNumber[first] - 1).getFigure() != imageInfomationList.get(randomNumber[second] - 1).getFigure()
                                    && imageInfomationList.get(randomNumber[second] - 1).getFigure() != imageInfomationList.get(randomNumber[third] - 1).getFigure()
                                    && imageInfomationList.get(randomNumber[first] - 1).getFigure() != imageInfomationList.get(randomNumber[third] - 1).getFigure()) {
                                isCheckFigure = true;
                            }//모두 다른 figure

                            if (imageInfomationList.get(randomNumber[first] - 1).getFigureColor() == imageInfomationList.get(randomNumber[second] - 1).getFigureColor()
                                    && imageInfomationList.get(randomNumber[second] - 1).getFigureColor() == imageInfomationList.get(randomNumber[third] - 1).getFigureColor()) {
                                isCheckFigureColor = true;
                            }//모두같
                            else if (imageInfomationList.get(randomNumber[first] - 1).getFigureColor() != imageInfomationList.get(randomNumber[second] - 1).getFigureColor()
                                    && imageInfomationList.get(randomNumber[second] - 1).getFigureColor() != imageInfomationList.get(randomNumber[third] - 1).getFigureColor()
                                    && imageInfomationList.get(randomNumber[first] - 1).getFigureColor() != imageInfomationList.get(randomNumber[third] - 1).getFigureColor()) {
                                isCheckFigureColor = true;
                            }

                            if (isCheckBackgroundColor == true && isCheckFigure == true && isCheckFigureColor == true) {//합이 되는 경우라면.
                                //비교 111 222 이런거  중복 안되게끔.
                                for (int i = 0; i < registAllCombination.size(); i++) {// 123  321 같은 경우를 방지하기 위해
                                    if ((int) (registAllCombination.get(i) / 100) == (first + 1) || (int) (registAllCombination.get(i) / 100) == (second + 1)
                                            || (int) (registAllCombination.get(i) / 100) == (third + 1)) {
                                        isThreeCheck[0] = true;
                                    }
                                    if ((int) ((registAllCombination.get(i) % 100) / 10) == (first + 1) || (int) ((registAllCombination.get(i) % 100) / 10) == (second + 1)
                                            || (int) ((registAllCombination.get(i) % 100) / 10) == (third + 1)) {
                                        isThreeCheck[1] = true;
                                    }
                                    if ((int) ((registAllCombination.get(i) % 100) % 10) == (first + 1) || (int) ((registAllCombination.get(i) % 100) % 10) == (second + 1)
                                            || (int) ((registAllCombination.get(i) % 100) % 10) == (third + 1)) {
                                        isThreeCheck[2] = true;
                                    }/*체크끝*/
                                    /**************/
                                    if (isThreeCheck[0] == true && isThreeCheck[1] == true && isThreeCheck[2] == true) {
                                        isOverLap = true;
                                        break;
                                    } else {
                                        isThreeCheck[0] = false;
                                        isThreeCheck[1] = false;
                                        isThreeCheck[2] = false;
                                    }
                                }//for
                                if (isOverLap == false) {
                                    combinationNumberList += Integer.toString(first + 1); //+1하는 이유는 인덱스이기떄문에
                                    combinationNumberList += Integer.toString(second + 1);
                                    combinationNumberList += Integer.toString(third + 1); //성공한 조합 문자열에 추가
                                    combinationNumberList += "\n";

                                    combinationCount++; //합의 총 개수 추가

                                    registAllCombination.add(((first + 1) * 100) + ((second + 1) * 10) + third + 1); //string형으로 저장해서 검사할떄마다 int형으로 바꿔 비교할수도있다 but 귀찮음

                                    isThreeCheck[0] = false;
                                    isThreeCheck[1] = false;
                                    isThreeCheck[2] = false; //불리언 초기화
                                    isCheckBackgroundColor = false;
                                    isCheckFigure = false;
                                    isCheckFigureColor = false;

                                    break; //합이 한번 나왔으면 second++하러 넘어가기
                                }//중복아닌 합 등록하자
                                else if (isOverLap == true) {
                                    isThreeCheck[0] = false;
                                    isThreeCheck[1] = false;
                                    isThreeCheck[2] = false;
                                    isOverLap = false;
                                }
                            }//3check if
                            isCheckBackgroundColor = false;
                            isCheckFigure = false;
                            isCheckFigureColor = false; //합이 존재하지않을 경우에도
                        }//검사 다 포함하는 if
                    }//third
                }//second for
            }//for
        }

        public void addThread(){
            timerThread=new Thread(new Runnable() {
                @Override
                public void run() {
                    for (sec=60; sec > 0 ; sec--) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                        if(!isThreadEnd){
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progBar.setProgress(sec);
                                    roundText.setText(""+gameRound+" ROUND");

                                    //progBar.incrementProgressBy(-1);
                                }//post run
                            });//hander post

                            if(gameRound < 11){
                                if(sec == 1){
                                    sec=65; gameRound++;

                                    waitIntent.putExtra("FP_score",FP_gamePoint);
                                    waitIntent.putExtra("SP_score",SP_gamePoint);
                                    waitIntent.putExtra("gameRound",gameRound);

                                    makeRandomNumber();//랜덤수 생성
                                    makeGameBoard();

                                    combinationNumberList = ""; //힌트 조합리스트 초기화
                                    CombinationResultStr=""; //입력한 매치히스토리 초기화

                                    registAllCombination.clear();//검사할때 들어갔던 모든합의 조합 초기화
                                    combinationCount=0; //검사할때 세었던 카운트 값 초기화
                                    registCombinationHistory.clear(); // 유저가 입력했던 합의 모든 조합 초기화

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            hintText.setText("");
                                            CombinationResult.setText(CombinationResultStr);
                                        }
                                    });

                                    allCheckCombinationFunction();//검사
                                    startActivity(waitIntent);
                                    //게임라운드 증가 추가하자
                                }
                            }else if(gameRound > 10){
                                //endGame();
                                GameEndIntent.putExtra("1P_id",FP_id); //아이디
                                GameEndIntent.putExtra("2P_id",SP_id);
                                GameEndIntent.putExtra("FP_gameScore",FP_gamePoint); //점수
                                GameEndIntent.putExtra("SP_gameScore",SP_gamePoint);
                                sec=0;

                                startActivity(GameEndIntent);
                            }
                        }else{
                            Log.d("Dead","Dead");
                            Thread.currentThread().interrupt();
                        }//else
                    }//for
                    Log.d("Thread kill","thread 죽음");
                    /**프로그레스바 다 줄어들고 튜토리얼 종료**/
                }//Thread run
            });//timerThread
        }//addThread
        public Boolean checkOverLapFunction() {
            //여기서 이미 있는 값이라면 isOverLap = false 아니라면 = true;
            String combination = "";

            combination = "" + pressedImageNumber[0] + pressedImageNumber[1] + pressedImageNumber[2];

            if (registCombinationHistory.size() == 0) {//처음엔
                return false; //처음이어도 false.
            } else {
                for (int i = 0; i < registCombinationHistory.size(); i++) {
                    if (combination.equals(registCombinationHistory.get(i))) {//입력받은 조합이 저장된 히스토리에 존재한다면.
                        return true;//중복임
                    }
                }//for
            }
            return false;
        }

        public void endGame(){ //게임 종료함수
            GameEndIntent.putExtra("1P_id",FP_id); //아이디
            GameEndIntent.putExtra("2P_id",SP_id);
            GameEndIntent.putExtra("FP_gameScore",FP_gamePoint); //점수
            GameEndIntent.putExtra("SP_gameScore",SP_gamePoint);

            startActivity(GameEndIntent);
        }
    }//class


