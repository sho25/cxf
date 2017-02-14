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
name|jaxws
operator|.
name|handler
operator|.
name|types
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
name|XmlAccessType
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
name|XmlAccessorType
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

begin_comment
comment|/**  * The elements that use this type designate either a relative path or an absolute path starting with a "/".  * In elements that specify a pathname to a file within the same Deployment File, relative filenames (i.e.,  * those not starting with "/") are considered relative to the root of the Deployment File's namespace.  * Absolute filenames (i.e., those starting with "/") also specify names in the root of the Deployment File's  * namespace. In general, relative names are preferred. The exception is .war files where absolute names are  * preferred for consistency with the Servlet API.  *<p>  * Java class for pathType complex type.  *<p>  * The following schema fragment specifies the expected content contained within this class.  *  *<pre>  *&lt;complexType name="pathType">  *&lt;simpleContent>  *&lt;restriction base="&lt;http://java.sun.com/xml/ns/javaee>string">  *&lt;/restriction>  *&lt;/simpleContent>  *&lt;/complexType>  *</pre>  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"pathType"
argument_list|)
specifier|public
class|class
name|PathType
extends|extends
name|CString
block|{  }
end_class

end_unit

