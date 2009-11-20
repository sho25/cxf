begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * JAX-RS specific logging support. Based on<tt>java.util.logging</tt> (JUL)  * with use of different logging frameworks factored out; assumes that client   * with source code logging to other systems, like Log4J, can bridge   * to this implementation applying<a href="www.slf4j.org">SLF4J</a>   * that JAXRS already depends on.  */
end_comment

begin_annotation
annotation|@
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlSchema
argument_list|(
name|xmlns
operator|=
block|{
annotation|@
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlNs
argument_list|(
name|namespaceURI
operator|=
literal|"http://cxf.apache.org/jaxrs/log"
argument_list|,
name|prefix
operator|=
literal|"log"
argument_list|)
block|}
argument_list|)
end_annotation

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|logging
package|;
end_package

end_unit

