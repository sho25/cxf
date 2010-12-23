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
name|systest
operator|.
name|jaxrs
operator|.
name|jaxws
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|feature
operator|.
name|Features
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|Book
import|;
end_import

begin_class
annotation|@
name|Features
argument_list|(
name|features
operator|=
literal|"org.apache.cxf.feature.FastInfosetFeature"
argument_list|)
specifier|public
class|class
name|BookStoreSoapRestFastInfoset3
extends|extends
name|BookStoreSoapRestImpl
block|{
annotation|@
name|Override
specifier|public
name|Book
name|addFastinfoBook
parameter_list|(
name|Book
name|book
parameter_list|)
block|{
return|return
name|super
operator|.
name|addFastinfoBook
argument_list|(
name|book
argument_list|)
return|;
block|}
block|}
end_class

end_unit

