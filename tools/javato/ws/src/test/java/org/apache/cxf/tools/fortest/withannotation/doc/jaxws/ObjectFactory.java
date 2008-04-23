begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|fortest
operator|.
name|withannotation
operator|.
name|doc
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRegistry
import|;
end_import

begin_comment
comment|/**  * This object contains factory methods for each   * Java content interface and Java element interface   * generated in the org.apache.cxf.tools.fortest.classnoanno.docwrapped.jaxws package.   *<p>An ObjectFactory allows you to programatically   * construct new instances of the Java representation   * for XML content. The Java representation of XML   * content can consist of schema derived interfaces   * and classes representing the binding of schema   * type definitions, element declarations and model   * groups.  Factory methods for each of these are   * provided in this class.  *   */
end_comment

begin_class
annotation|@
name|XmlRegistry
specifier|public
class|class
name|ObjectFactory
block|{
comment|/**      * Create a new ObjectFactory that can be used to       * create new instances of schema derived classes       * for package: org.apache.cxf.tools.fortest.classnoanno.docwrapped.jaxws      *       */
specifier|public
name|ObjectFactory
parameter_list|()
block|{     }
comment|/**      * Create an instance of {@link GetPrice }      *       */
specifier|public
name|GetPrice
name|createGetPrice
parameter_list|()
block|{
return|return
operator|new
name|GetPrice
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link GetPriceResponse }      *       */
specifier|public
name|GetPriceResponse
name|createGetPriceResponse
parameter_list|()
block|{
return|return
operator|new
name|GetPriceResponse
argument_list|()
return|;
block|}
block|}
end_class

end_unit

