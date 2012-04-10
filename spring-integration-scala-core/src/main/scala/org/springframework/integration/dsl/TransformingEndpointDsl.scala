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
import org.springframework.beans.factory.support.BeanDefinitionBuilder
import org.springframework.integration.config.TransformerFactoryBean
import org.springframework.util.StringUtils
import java.util.UUID

/**
 * This class provides DSL and related components to support "Message Transformer" pattern
 * 
 * @author Oleg Zhurakousky
 */
object transform {

  def using(function:Function1[_,AnyRef]) = new SendingEndpointComposition(null, new Transformer(target = function)) {
    def where(name:String) = { 
      require(StringUtils.hasText(name), "'name' must not be empty")
      new SendingEndpointComposition(null, new Transformer(name = name, target = function))
    }
  }
  
  def using(targetObject:Object) = new SendingEndpointComposition(null, new Transformer(target = targetObject)) {
    def where(name:String)= {
      require(StringUtils.hasText(name), "'name' must not be empty")
      new SendingEndpointComposition(null, new Transformer(name = name, target = targetObject))
    }
  }
}

private[dsl] class Transformer(name:String = "$xfmr_" + UUID.randomUUID().toString.substring(0, 8), target:Any)
            extends SimpleEndpoint(name, target) {
  
  override def build(targetDefFunction: Function2[SimpleEndpoint, BeanDefinitionBuilder, Unit],
                     compositionInitFunction: Function2[BaseIntegrationComposition, AbstractChannel, Unit]): BeanDefinitionBuilder = {
    val handlerBuilder = BeanDefinitionBuilder.rootBeanDefinition(classOf[TransformerFactoryBean])
    targetDefFunction.apply(this, handlerBuilder)
    handlerBuilder
  }
}