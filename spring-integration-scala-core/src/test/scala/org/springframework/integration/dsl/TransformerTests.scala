/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.dsl
import org.junit.{Assert, Test}
/**
 * @author Oleg Zhurakousky
 */
class TransformerTests {

  @Test
  def validateTransformerWithJavaObjectAsTarget {

    val messageFlow = transform.using(new SimpleTransformer)
    
    val reply =  messageFlow.sendAndReceive[String]("Hello Java")
    Assert.assertEquals(reply, "HELLO JAVA")
  }
  
  @Test
  def validateTransformerWithFunctionAsTargetInvokingJavaObject {

    val service = new SimpleTransformer
    
    val messageFlow = transform.using{payload:String => service.transform(payload)}
    
    val reply =  messageFlow.sendAndReceive[String]("Hello Java")
    Assert.assertEquals(reply, "HELLO JAVA")
  }
}