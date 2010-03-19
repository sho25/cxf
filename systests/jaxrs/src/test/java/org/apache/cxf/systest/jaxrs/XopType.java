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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

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
name|XmlElement
import|;
end_import

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
name|XmlMimeType
import|;
end_import

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
name|XmlRootElement
import|;
end_import

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
name|XmlType
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"xopType"
argument_list|,
name|namespace
operator|=
literal|"http://xop/jaxrs"
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"XopType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"name"
block|,
literal|"attachinfo"
block|,
literal|"attachinfo2"
block|,
literal|"image"
block|}
argument_list|)
specifier|public
class|class
name|XopType
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|DataHandler
name|attachinfo
decl_stmt|;
specifier|private
name|byte
index|[]
name|attachinfo2
decl_stmt|;
specifier|private
name|Image
name|image
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|XmlMimeType
argument_list|(
literal|"application/octet-stream"
argument_list|)
specifier|public
name|byte
index|[]
name|getAttachinfo2
parameter_list|()
block|{
return|return
name|attachinfo2
return|;
block|}
specifier|public
name|void
name|setAttachinfo2
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
name|this
operator|.
name|attachinfo2
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|XmlMimeType
argument_list|(
literal|"application/octet-stream"
argument_list|)
specifier|public
name|DataHandler
name|getAttachinfo
parameter_list|()
block|{
return|return
name|attachinfo
return|;
block|}
specifier|public
name|void
name|setAttachinfo
parameter_list|(
name|DataHandler
name|value
parameter_list|)
block|{
name|this
operator|.
name|attachinfo
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|setImage
parameter_list|(
name|Image
name|image
parameter_list|)
block|{
name|this
operator|.
name|image
operator|=
name|image
expr_stmt|;
block|}
specifier|public
name|Image
name|getImage
parameter_list|()
block|{
return|return
name|image
return|;
block|}
block|}
end_class

end_unit

