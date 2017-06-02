/*
 * The MIT License
 *
 * Copyright 2017 AnggaraNothing.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.anggaranothing.pakar.dyslexia.impl;

/**
 *
 * @author AnggaraNothing
 */
public class InferenceEngine {
    public static float cfHE( float cfExpert, float cfUser ) {
        return cfExpert * cfUser;
    }
    public static float cfCombine( float fl1, float fl2 ) {
        
        // Default: both positives
        float result = fl1 + fl2 * ( 1 - fl1 );
        
        // both negatives
        if( fl1 < 0 && fl2 < 0 ) {
            result = fl1 + fl2 * ( 1 + fl1 );
        }
        // one of them is negative
        else if( fl1 < 0 || fl2 < 0 ) {
            result = fl1 + fl2 / 1 - Math.min( fl1, fl2 );
        }
        
        return result;
    }
}
